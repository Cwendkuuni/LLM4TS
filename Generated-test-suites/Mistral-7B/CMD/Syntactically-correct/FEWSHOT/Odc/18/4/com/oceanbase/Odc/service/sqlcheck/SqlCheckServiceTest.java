package com.oceanbase.odc.service.sqlcheck;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.jdbc.JdbcTemplate;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceUtils;

import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionConstants;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.service.collaboration.environment.Environment;
import com.oceanbase.odc.service.collaboration.environment.EnvironmentService;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.regulation.ruleset.Rule;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.model.QueryRuleMetadataParams;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule.RuleViolation;
import com.oceanbase.odc.service.regulation.ruleset.model.RuleMetadata;
import com.oceanbase.odc.service.regulation.ruleset.model.RuleType;
import com.oceanbase.odc.service.session.factory.OBConsoleDataSourceFactory;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRules;
import com.oceanbase.odc.service.sqlcheck.service.SqlCheckService;
import com.oceanbase.odc.service.sqlcheck.util.SqlCheckUtil;
import com.oceanbase.odc.service.sqlcheck.util.SqlChecker;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class SqlCheckServiceTest {

    @Test
    public void testCheck() {
        RuleService ruleServiceMock = mock(RuleService.class);
        EnvironmentService environmentServiceMock = mock(EnvironmentService.class);
        ConnectionSession sessionMock = mock(ConnectionSession.class);
        SqlCheckReq req = new SqlCheckReq();
        when(sessionMock.getDialectType()).thenReturn(DialectType.MYSQL);
        when(sessionMock.getSyncJdbcExecutor(ConnectionSessionConstants.CONSOLE_DS_KEY)).thenReturn(new JdbcTemplate(getMockDataSource()));
        SqlCheckService sqlCheckService = new SqlCheckService() {{
            this.ruleService = ruleServiceMock;
            this.environmentService = environmentServiceMock;
        }};

        List<Rule> rules = new ArrayList<>();
        Rule rule1 = new Rule();
        RuleMetadata metadata1 = new RuleMetadata();
        metadata1.setName("rule1");
        metadata1.setType(RuleType.SQL_CHECK);
        metadata1.setLevel(2);
        rule1.setMetadata(metadata1);
        rule1.setEnabled(true);
        rules.add(rule1);

        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, sessionMock);
        assertEquals(1, sqlCheckRules.size());

        SqlChecker sqlCheckerMock = mock(SqlChecker.class);
        List<CheckViolation> checkViolations = new ArrayList<>();
        CheckViolation violation1 = new CheckViolation();
        violation1.setType(RuleViolation.Type.SQL_INJECTION);
        violation1.setLevel(2);
        checkViolations.add(violation1);

        when(sqlCheckerMock.check(req.getScriptContent())).thenReturn(checkViolations);

        when(ruleServiceMock.list(anyLong(), eq(QueryRuleMetadataParams.builder().build()))).thenReturn(rules);
        when(environmentServiceMock.detail(anyLong())).thenReturn(new Environment());

        List<CheckResult> result = sqlCheckService.check(sessionMock, req);

        assertEquals(1, result.size());
        assertEquals(violation1.getLevel(), result.get(0).getViolations().get(0).getLevel());
    }

    @Test
    public void testCheckEnvironment() {
        RuleService ruleServiceMock = mock(RuleService.class);
        EnvironmentService environmentServiceMock = mock(EnvironmentService.class);
        ConnectionConfig config = new ConnectionConfig();
        List<OffsetString> sqls = new ArrayList<>();
        OffsetString sql1 = new OffsetString("SELECT * FROM users WHERE name = 'admin'");
        sqls.add(sql1);

        SqlCheckService sqlCheckService = new SqlCheckService() {{
            this.ruleService = ruleServiceMock;
            this.environmentService = environmentServiceMock;
        }};

        List<Rule> rules = new ArrayList<>();
        Rule rule1 = new Rule();
        RuleMetadata metadata1 = new RuleMetadata();
        metadata1.setName("rule1");
        metadata1.setType(RuleType.SQL_CHECK);
        metadata1.setLevel(2);
        rule1.setMetadata(metadata1);
        rule1.setEnabled(true);
        rules.add(rule1);

        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, config.getDialectType(), getMockDataSource());
        assertEquals(1, sqlCheckRules.size());

        DefaultSqlChecker sqlCheckerMock = mock(DefaultSqlChecker.class);
        List<CheckViolation> checkViolations = new ArrayList<>();
        CheckViolation violation1 = new CheckViolation();
        violation1.setType(RuleViolation.Type.SQL_INJECTION);
        violation1.setLevel(2);
        checkViolations.add(violation1);

        when(sqlCheckerMock.check(sqls, new SqlCheckContext(sqls.size()))).thenReturn(checkViolations);
        when(ruleServiceMock.list(anyLong(), eq(QueryRuleMetadataParams.builder().build()))).thenReturn(rules);

        List<CheckResult> result = sqlCheckService.check(1L, "testdb", sqls, config);

        assertEquals(1, result.size());
        assertEquals(violation1.getLevel(), result.get(0).getViolations().get(0).getLevel());
    }

    @Test
    public void testEmptyCheck() {
        RuleService ruleServiceMock = mock(RuleService.class);
        EnvironmentService environmentServiceMock = mock(EnvironmentService.class);
        ConnectionSession sessionMock = mock(ConnectionSession.class);
        SqlCheckReq req = new SqlCheckReq();
        when(sessionMock.getDialectType()).thenReturn(DialectType.MYSQL);
        when(sessionMock.getSyncJdbcExecutor(ConnectionSessionConstants.CONSOLE_DS_KEY)).thenReturn(new JdbcTemplate(getMockDataSource()));
        SqlCheckService sqlCheckService = new SqlCheckService() {{
            this.ruleService = ruleServiceMock;
            this.environmentService = environmentServiceMock;
        }};

        when(ruleServiceMock.list(anyLong(), eq(QueryRuleMetadataParams.builder().build()))).thenReturn(Collections.emptyList());
        List<CheckResult> result = sqlCheckService.check(sessionMock, req);
        assertEquals(0, result.size());
    }

    private JdbcOperations getMockDataSource() {
        BasicDataSource ds = new BasicDataSource();
        ds.setDriverClassName("com.mysql.jdbc.Driver");
        ds.setUrl("jdbc:mysql://localhost:3306/testdb");
        ds.setUsername("root");
        ds.setPassword("password");
        return new JdbcTemplate(ds);
    }
}