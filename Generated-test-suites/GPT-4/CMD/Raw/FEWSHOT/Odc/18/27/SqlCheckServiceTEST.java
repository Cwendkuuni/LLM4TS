package com.oceanbase.odc.service.sqlcheck;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;
import com.oceanbase.odc.core.sql.split.OffsetString;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;
import static org.mockito.Mockito.*;

public class SqlCheckServiceTest {
    private SqlCheckService sqlCheckService;
    private RuleService ruleServiceMock;
    private EnvironmentService environmentServiceMock;
    private ConnectionSession sessionMock;
    private ConnectionConfig configMock;
    private List<OffsetString> sqlsMock;
    
    
    @Before
    public void setUp() {
        ruleServiceMock = mock(RuleService.class);
        environmentServiceMock = mock(EnvironmentService.class);
        sessionMock = mock(ConnectionSession.class);
        configMock = mock(ConnectionConfig.class);
        sqlsMock = new ArrayList<OffsetString>();
        sqlCheckService = new SqlCheckService();
        sqlCheckService.setRuleService(ruleServiceMock);
        sqlCheckService.setEnvironmentService(environmentServiceMock);
    }
    
    @Test
    public void testCheck_withSessionAndReq() {
        SqlCheckReq reqMock = mock(SqlCheckReq.class);
        when(reqMock.getScriptContent()).thenReturn("select * from table");
        when(reqMock.getDelimiter()).thenReturn(";");
        when(sessionMock.getDialectType()).thenReturn(DialectType.MYSQL);
        List<CheckResult> checkResult = sqlCheckService.check(sessionMock,reqMock);
        assertNotNull(checkResult);
    }
    
    @Test
    public void testCheck_withEnvironmentIdDatabaseNameAndSqls() {
        when(configMock.getDialectType()).thenReturn(DialectType.MYSQL);
        List<CheckViolation> checkViolation = sqlCheckService.check(1L,"DBname",sqlsMock, configMock);  
        assertNotNull(checkViolation);
    }
    
    @Test
    public void testGetRules_withRulesAndSession() {
        List<Rule> rulesMock = mock(ArrayList.class);
        when(sessionMock.getDialectType()).thenReturn(DialectType.MYSQL);
        List<SqlCheckRule> sqlCheckRule = sqlCheckService.getRules(rulesMock,sessionMock);
        assertNotNull(sqlCheckRule);
    }

    @Test
    public void testFullFillRiskLevel_withRulesAndViolations() {
        List<Rule> rulesMock = mock(ArrayList.class);
        List<CheckViolation> violationsMock = mock(ArrayList.class);
        List<Rule> resultingRules = sqlCheckService.fullFillRiskLevel(rulesMock,violationsMock);
        assertNotNull(resultingRules);
    }
}