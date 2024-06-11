package com.oceanbase.odc.service.sqlcheck;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.collaboration.environment.EnvironmentService;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.connection.model.OffsetString;
import com.oceanbase.odc.core.shared.constant.DialectType;

public class SqlCheckServiceTest {

    @InjectMocks
    private SqlCheckService sqlCheckService;

    @Mock
    private RuleService ruleService;

    @Mock
    private EnvironmentService environmentService;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCheckWithSession() {
        ConnectionSession session = new ConnectionSession();
        SqlCheckReq req = new SqlCheckReq();
        req.setScriptContent("SELECT * FROM table");
        
        List<CheckResult> result = sqlCheckService.check(session, req);
        
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testCheckWithEnvironment() {
        Long environmentId = 1L;
        String databaseName = "test_db";
        List<OffsetString> sqls = new ArrayList<>();
        ConnectionConfig config = new ConnectionConfig(DialectType.MYSQL);
        
        List<CheckViolation> result = sqlCheckService.check(environmentId, databaseName, sqls, config);
        
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetRules() {
        List<CheckViolation> violations = new ArrayList<>();
        
        List<SqlCheckRule> rules = sqlCheckService.getRules(new ArrayList<>(), DialectType.MYSQL, Mockito.mock(JdbcTemplate.class));
        
        assertNotNull(rules);
        assertTrue(rules.isEmpty());
    }

    @Test
    public void testFullFillRiskLevel() {
        List<CheckViolation> violations = new ArrayList<>();
        
        List<CheckViolation> result = sqlCheckService.fullFillRiskLevel(new ArrayList<>(), violations);
        
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}