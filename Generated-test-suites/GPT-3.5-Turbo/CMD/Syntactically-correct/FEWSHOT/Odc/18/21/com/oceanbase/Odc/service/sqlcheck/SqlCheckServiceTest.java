package com.oceanbase.odc.service.sqlcheck;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.oceanbase.odc.core.dialect.DialectType;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionConstants;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRule;

public class SqlCheckServiceTest {

    private SqlCheckService sqlCheckService;
    private ConnectionSession connectionSession;

    @Before
    public void setUp() {
        sqlCheckService = new SqlCheckService();
        connectionSession = mock(ConnectionSession.class);
        when(connectionSession.getDialectType()).thenReturn(DialectType.MYSQL);
        when(connectionSession.getSyncJdbcExecutor(ConnectionSessionConstants.CONSOLE_DS_KEY))
                .thenReturn(null);
    }

    @Test
    public void testCheckWithSession() {
        SqlCheckReq req = new SqlCheckReq();
        req.setScriptContent("SELECT * FROM table;");
        List<CheckResult> checkResults = sqlCheckService.check(connectionSession, req);
        assertNotNull(checkResults);
    }

    @Test
    public void testCheckWithEnvironmentId() {
        Long environmentId = 1L;
        String databaseName = "test_db";
        List<OffsetString> sqls = Collections.singletonList(new OffsetString(0, 15));
        List<CheckViolation> checkViolations = sqlCheckService.check(environmentId, databaseName, sqls, null);
        assertNotNull(checkViolations);
    }

    @Test
    public void testGetRulesWithSession() {
        List<SqlCheckRule> rules = sqlCheckService.getRules(Collections.emptyList(), connectionSession);
        assertNotNull(rules);
    }

    @Test
    public void testGetRulesWithJdbc() {
        List<SqlCheckRule> rules = sqlCheckService.getRules(Collections.emptyList(), DialectType.MYSQL, null);
        assertNotNull(rules);
    }

    @Test
    public void testFullFillRiskLevel() {
        List<CheckViolation> violations = new ArrayList<>();
        violations.add(new CheckViolation("type1", "message1"));
        violations.add(new CheckViolation("type2", "message2"));
        List<CheckViolation> checkViolations = sqlCheckService.fullFillRiskLevel(Collections.emptyList(), violations);
        assertNotNull(checkViolations);
    }
}