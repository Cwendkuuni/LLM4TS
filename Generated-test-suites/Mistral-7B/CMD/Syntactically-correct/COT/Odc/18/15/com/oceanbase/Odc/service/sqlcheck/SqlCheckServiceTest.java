package com.oceanbase.odc.service.sqlcheck;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;

import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionConstants;
import com.oceanbase.odc.service.collaboration.environment.Environment;
import com.oceanbase.odc.service.collaboration.environment.model.EnvironmentService;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.regulation.ruleset.Model.CheckResult;
import com.oceanbase.odc.service.regulation.ruleset.Model.CheckViolation;
import com.oceanbase.odc.service.regulation.ruleset.Model.Rule;
import com.oceanbase.odc.service.regulation.ruleset.Model.Rule.RuleViolation;
import com.oceanbase.odc.service.regulation.ruleset.Model.RuleMetadata;
import com.oceanbase.odc.service.regulation.ruleset.Model.RuleType;
import com.oceanbase.odc.service.session.factory.OBConsoleDataSourceFactory;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRules;
import com.oceanbase.odc.service.sqlcheck.service.SqlCheckService;
import com.oceanbase.odc.service.sqlcheck.util.SqlCheckUtil;
import com.oceanbase.odc.service.sqlcheck.util.SqlChecker;

public class SqlCheckServiceTest {

    @InjectMocks
    private SqlCheckService sqlCheckService;

    private ConnectionSession session;
    private SqlCheckReq sqlCheckReq;
    private List<Rule> rules;
    private List<CheckViolation> checkViolations;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        session = new ConnectionSession();
        sqlCheckReq = new SqlCheckReq();
        rules = new ArrayList<>();
        checkViolations = new ArrayList<>();
    }

    @Test
    public void testCheck() {
        Rule rule = new Rule();
        RuleMetadata metadata = new RuleMetadata();
        metadata.setName("TestRule");
        metadata.setType(RuleType.SQL_CHECK);
        rule.setMetadata(metadata);
        rule.setEnabled(true);
        rules.add(rule);

        SqlCheckRule sqlCheckRule = new DefaultSqlCheckRule(metadata.getName(), "Test", 1, "TestErrorMessage");
        sqlCheckRules.register(sqlCheckRule);

        JdbcOperations jdbcOperations = new JdbcTemplate(new JdbcTemplate());
        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, jdbcOperations.getClass(), jdbcOperations);

        SqlChecker sqlChecker = new DefaultSqlChecker(jdbcOperations.getClass(), null, sqlCheckRules);
        CheckViolation violation = new CheckViolation();
        violation.setType(rule.getMetadata().getName());
        violation.setMessage("TestErrorMessage");
        checkViolations.add(violation);

        String scriptContent = "TestScript";
        List<CheckResult> checkResults = sqlCheckService.check(session, sqlCheckReq);
        assertEquals(Collections.singletonList(checkResults), SqlCheckUtil.buildCheckResults(checkViolations));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckNullSession() {
        sqlCheckService.check(null, sqlCheckReq);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckNullReq() {
        sqlCheckService.check(session, null);
    }

    @Test(expected = NullPointerException.class)
    public void testCheckNullRules() {
        List<Rule> rules = null;
        sqlCheckService.fullFillRiskLevel(rules, checkViolations);
    }

    @Test(expected = NullPointerException.class)
    public void testCheckNullViolations() {
        sqlCheckService.fullFillRiskLevel(rules, null);
    }
}