package com.oceanbase.odc.service.sqlcheck;

import org.junit.Test;
import org.junit.Assert;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;

public class SqlCheckServiceTest {

    @Mock
    private RuleService ruleService;

    @Mock
    private EnvironmentService environmentService;

    @Mock
    private ConnectionSession session;

    @Mock
    private SqlCheckReq req;

    @Mock
    private ConnectionConfig config;

    @Mock
    private SqlCheckService sqlCheckService;

    @Test
    public void testCheck() {
        when(session.getDialectType()).thenReturn(DialectType.MYSQL);
        when(req.getDelimiter()).thenReturn(";");
        when(sqlCheckService.getRules(Mockito.anyList(), session)).thenReturn(new ArrayList<>());
        
        List<CheckResult> result = sqlCheckService.check(session, req);
        Assert.assertNotNull(result);
    }

    @Test
    public void testCheckWithEnvironment() {
        Long environmentId = 1L;
        String databaseName = "test";
        List<OffsetString> sqls = new ArrayList<>();
        
        when(environmentService.detail(environmentId)).thenReturn(new Environment());
        when(ruleService.list(Mockito.anyLong(), Mockito.any())).thenReturn(new ArrayList<>());
        when(sqlCheckService.getRules(Mockito.anyList(), Mockito.any(DialectType.class), Mockito.any(JdbcOperations.class)))
            .thenReturn(new ArrayList<>());
        
        List<CheckViolation> violations = sqlCheckService.check(environmentId, databaseName, sqls, config);
        Assert.assertNotNull(violations);
    }

    @Test
    public void testGetRules() {
        List<Rule> rules = new ArrayList<>();
        ConnectionSession session = new ConnectionSession();
        JdbcTemplate jdbc = new JdbcTemplate();
        
        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, session);
        Assert.assertNotNull(sqlCheckRules);
    }

    @Test
    public void testFullFillRiskLevel() {
        List<Rule> rules = new ArrayList<>();
        List<CheckViolation> violations = new ArrayList<>();
        
        List<Rule> violatedRules = sqlCheckService.fullFillRiskLevel(rules, violations);
        Assert.assertNotNull(violatedRules);
    }

}