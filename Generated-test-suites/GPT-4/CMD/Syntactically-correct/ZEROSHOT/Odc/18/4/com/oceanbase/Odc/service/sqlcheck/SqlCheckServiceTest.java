package com.oceanbase.odc.service.sqlcheck;


import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SqlCheckServiceTest {

    @InjectMocks
    private SqlCheckService sqlCheckService;

    @Mock
    private RuleService ruleService;

    @Mock
    private EnvironmentService environmentService;

    @Mock
    private ConnectionSession session;

    @Mock
    private SqlCheckReq req;

    private List<Rule> rules = new ArrayList<>();

    @Before
    public void setup() {
        rules.add(new Rule());
        when(ruleService.list(1L, QueryRuleMetadataParams.builder().build())).thenReturn(rules);
    }

    @Test
    public void check_ReturnsEmptyList_WhenRuleSetIdIsNull() {
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(null);
        List<CheckResult> checkResults = sqlCheckService.check(session, req);

        assertEquals("check() should return an empty list when ruleSetId is null", 0, checkResults.size());
    }

    @Test
    public void check_ReturnsEmptyList_WhenNoSqlCheckRulesAreFound() {
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);
        List<CheckResult> checkResults = sqlCheckService.check(session, req);

        assertEquals("check() should return an empty list when no SqlCheckRules are found", 0, checkResults.size());
    }

    @Test
    public void check_FulfillsRiskLevel_WhenCheckViolationsAreFound() {
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);
        when(req.getDelimiter()).thenReturn(";");
        when(req.getScriptContent()).thenReturn("SELECT * FROM user;");

        List<CheckResult> checkResults = sqlCheckService.check(session, req);

        assertNotEquals("check() should not return an empty list when check violations are found", 0, checkResults.size());
    }

    @Test
    public void checkWithEnvironmentId_ReturnsEmptyList_WhenNoSqlsAreProvided() {
        List<CheckViolation> checkViolations = sqlCheckService.check(1L, "databaseName", new ArrayList<>(), new ConnectionConfig());

        assertEquals("check() should return an empty list when no SQL strings are provided", 0, checkViolations.size());
    }

    @Test
    public void checkWithEnvironmentId_FulfillsRiskLevel_WhenCheckViolationsAreFound() {
        List<OffsetString> offsetStrings = Arrays.asList(new OffsetString("SELECT * FROM user;"));
        List<CheckViolation> checkViolations = sqlCheckService.check(1L, "databaseName", offsetStrings, new ConnectionConfig());

        assertNotEquals("check() should not return an empty list when check violations are found", 0, checkViolations.size());
    }
}

