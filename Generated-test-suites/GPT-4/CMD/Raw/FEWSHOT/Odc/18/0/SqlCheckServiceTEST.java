package com.oceanbase.odc.service.sqlcheck;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;
import com.oceanbase.odc.core.sql.split.OffsetString;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.mockito.Mockito;
import org.mockito.ArgumentCaptor;
import org.springframework.jdbc.core.JdbcOperations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SqlCheckServiceTest {
    private RuleService ruleService = Mockito.mock(RuleService.class);
    private EnvironmentService environmentService = Mockito.mock(EnvironmentService.class);
    private ConnectionSession session = Mockito.mock(ConnectionSession.class);
    private ConnectionConfig config = Mockito.mock(ConnectionConfig.class);
    private JdbcOperations jdbc = Mockito.mock(JdbcOperations.class);
    
    @Test
    public void testCheck() {
        SqlCheckService service = new SqlCheckService();
        service.ruleService = ruleService;
        SqlCheckReq req = new SqlCheckReq();
        req.setDelimiter(";");
        req.setScriptContent("--test script content");
        
        List<CheckResult> result = service.check(session, req);
        
        // define your assertion here
    }
    
    @Test
    public void testCheckWithEnvironmentId() {
        SqlCheckService service = new SqlCheckService();
        service.ruleService = ruleService;
        service.environmentService = environmentService;

        String databaseName = "testDB";
        Long environmentId = 1L;
        List<OffsetString> sqls = new ArrayList<>();
        sqls.add(new OffsetString("--test sql", 0));
        
        List<CheckViolation> result = service.check(environmentId, databaseName, sqls, config);
        
        // define your assertion here
    }
    
    @Test
    public void testGetRulesWithSession() {
        SqlCheckService service = new SqlCheckService();
        service.ruleService = ruleService;
        
        List<Rule> rules = new ArrayList<>();
        
        List<SqlCheckRule> result = service.getRules(rules, session);

        // define your assertion here
    }
    
    @Test
    public void testGetRulesWithDialectType() {
        SqlCheckService service = new SqlCheckService();
        service.ruleService = ruleService;
        
        List<Rule> rules = new ArrayList<>();
        DialectType dialectType = DialectType.MYSQL;
        
        List<SqlCheckRule> result = service.getRules(rules, dialectType, jdbc);

        // define your assertion here
    }

    @Test
    public void testFullFillRiskLevel() {
        SqlCheckService service = new SqlCheckService();
        service.ruleService = ruleService;
        
        List<Rule> rules = new ArrayList<>();
        List<CheckViolation> violations = new ArrayList<>();

        List<Rule> result = service.fullFillRiskLevel(rules, violations);

        // define your assertion here
    }
}