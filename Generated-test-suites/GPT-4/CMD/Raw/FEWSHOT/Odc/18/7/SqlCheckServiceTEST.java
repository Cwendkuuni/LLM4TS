package com.oceanbase.odc.service.sqlcheck;

import static org.junit.Assert.*;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import java.util.*;
import org.junit.Test;
import org.junit.Before;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;

import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.sql.split.OffsetString;
import com.oceanbase.odc.service.collaboration.environment.EnvironmentService;
import com.oceanbase.odc.service.collaboration.environment.model.Environment;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;

public class SqlCheckServiceTest {
    
    SqlCheckService sqlCheckService;
    RuleService ruleServiceMock;
    EnvironmentService environmentServiceMock;

    @Before
    public void setup() {
        ruleServiceMock = Mockito.mock(RuleService.class);
        environmentServiceMock = Mockito.mock(EnvironmentService.class);
        sqlCheckService = new SqlCheckService();
        sqlCheckService.setRuleService(ruleServiceMock);
        sqlCheckService.setEnvironmentService(environmentServiceMock);
    }

    @Test
    public void testCheckWithConnectionSession() {
        ConnectionSession sessionMock = Mockito.mock(ConnectionSession.class);
        SqlCheckReq reqMock = Mockito.mock(SqlCheckReq.class);

        when(reqMock.getScriptContent()).thenReturn("SELECT * FROM table");

        List<CheckResult> result = sqlCheckService.check(sessionMock, reqMock);

        assertNotNull(result);
    }

    @Test
    public void testCheckWithEnvironmentId() {
        String databaseName = "testDatabase";
        List<OffsetString> sqls = Arrays.asList(new OffsetString("SELECT * FROM table"));
        ConnectionConfig configMock = Mockito.mock(ConnectionConfig.class);
        
        List<CheckViolation> result = sqlCheckService.check(1L, databaseName, sqls, configMock);
        
        assertNotNull(result);
    }

    @Test
    public void testGetRulesWithSession() {
        ConnectionSession sessionMock = Mockito.mock(ConnectionSession.class);
        List<Rule> rules = Collections.emptyList();

        List<SqlCheckRule> result = sqlCheckService.getRules(rules, sessionMock);

        assertNotNull(result);
    }

    @Test
    public void testGetRulesWithDialect() {
        JdbcTemplate jdbcMock = Mockito.mock(JdbcTemplate.class);
        List<Rule> rules = Collections.emptyList();

        List<SqlCheckRule> result = sqlCheckService.getRules(rules, DialectType.MYSQL, jdbcMock);

        assertNotNull(result);
    }

    @Test
    public void testFullFillRiskLevel() {
        List<CheckViolation> violationsMock = Mockito.mock(List.class);
        List<Rule> rulesMock = Mockito.mock(List.class);

        List<Rule> result = sqlCheckService.fullFillRiskLevel(rulesMock, violationsMock);

        assertNotNull(result);
    }
}