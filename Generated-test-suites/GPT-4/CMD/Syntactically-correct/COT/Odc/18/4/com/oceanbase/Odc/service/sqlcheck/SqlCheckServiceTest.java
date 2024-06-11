package com.oceanbase.odc.service.sqlcheck;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.*;
import javax.validation.ConstraintViolationException;
import org.junit.*;
import org.springframework.jdbc.core.JdbcOperations;

public class SqlCheckServiceTest {
    private RuleService mockRuleService;
    private EnvironmentService mockEnvironmentService;
    private SqlCheckService serviceUnderTest;
    // Set up object dependencies before starting the tests
    @Before
    public void setUp() {
        mockRuleService = mock(RuleService.class);
        mockEnvironmentService = mock(EnvironmentService.class);
        serviceUnderTest = new SqlCheckService();
        serviceUnderTest.setRuleService(mockRuleService);
        serviceUnderTest.setEnvironmentService(mockEnvironmentService);
    }

    @Test
    public void testCheck() {
        ConnectionSession mockSession = mock(ConnectionSession.class);
        SqlCheckReq mockReq = mock(SqlCheckReq.class);
        assertEquals(new ArrayList<>(), serviceUnderTest.check(mockSession, mockReq));
    }

    @Test
    public void testCheck_withEnvironment() {
        Long mockEnvironmentId = 1L;
        String mockDatabaseName = "TestDB";
        List<OffsetString> mockSqls = Collections.emptyList();
        ConnectionConfig mockConfig = mock(ConnectionConfig.class);
        assertEquals(new ArrayList<>(), serviceUnderTest.check(mockEnvironmentId, mockDatabaseName, mockSqls, mockConfig));
    }

    @Test
    public void testGetRules_Session() {
        List<Rule> mockRules = new ArrayList<>();
        ConnectionSession mockSession = mock(ConnectionSession.class);
        assertEquals(new ArrayList<>(), serviceUnderTest.getRules(mockRules, mockSession));
    }

    @Test
    public void testGetRules_DialectType() {
        List<Rule> mockRules = new ArrayList<>();
        DialectType mockType = DialectType.MySQL;
        JdbcOperations mockJdbc = mock(JdbcOperations.class);
        assertEquals(new ArrayList<>(), serviceUnderTest.getRules(mockRules, mockType, mockJdbc));
    }

    @Test
    public void testFullFillRiskLevel() {
        List<Rule> mockRules = new ArrayList<>();
        List<CheckViolation> mockViolations = new ArrayList<>();
        assertEquals(new ArrayList<>(), serviceUnderTest.fullFillRiskLevel(mockRules, mockViolations));
    }
}
