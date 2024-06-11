
package com.oceanbase.odc.service.sqlcheck;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import java.util.Arrays;
import com.oceanbase.odc.core.sql.split.OffsetString;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import static org.junit.Assert.assertEquals;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionConstants;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule;

import java.util.Collections;

import static org.mockito.Mockito.*;

public class SqlCheckServiceTest {

    @Autowired
    private SqlCheckService sqlCheckService;

    @Test
    public void testCheck_Session_SqlCheckReq() {
        ConnectionSession mockedSession = mock(ConnectionSession.class);
        SqlCheckReq mockedReq = mock(SqlCheckReq.class);

        when(mockedSession.getDialectType()).thenReturn(DialectType.MYSQL);
        when(mockedSession.getSyncJdbcExecutor(ConnectionSessionConstants.CONSOLE_DS_KEY)).thenReturn(new JdbcTemplate(ds));
        when(mockedReq.getScriptContent()).thenReturn("SELECT * FROM test;");

        List<CheckResult> results = sqlCheckService.check(mockedSession, mockedReq);

        assertEquals(0, results.size());
    }

    @Test
    public void testCheck_EnvironmentId_DatabaseName_Sqls_Config() {
        Long environmentId = 1L;
        String databaseName = "test";
        ConnectionConfig config = new ConnectionConfig();
        
        List<OffsetString> sqls = Arrays.asList(new OffsetString("SELECT * FROM TEST;"), new OffsetString("INSERT INTO TEST VALUES (1, 'test');"));

        List<CheckViolation> violations = sqlCheckService.check(environmentId, databaseName, sqls, config);

        assertEquals(0, violations.size());
    }

    @Test
    public void testCheck_GetRules_Session() {
        ConnectionSession mockedSession = mock(ConnectionSession.class);

        when(mockedSession.getDialectType()).thenReturn(DialectType.MYSQL);
        when(mockedSession.getSyncJdbcExecutor(ConnectionSessionConstants.CONSOLE_DS_KEY)).thenReturn(new JdbcTemplate(ds));

        List<SqlCheckRule> rules = sqlCheckService.getRules(Collections.emptyList(), mockedSession);

        assertEquals(0, rules.size());
    }

    @Test
    public void testCheck_GetRules_DialectType_JdbcOperations() {
        List<SqlCheckRule> rules = sqlCheckService.getRules(Collections.emptyList(), DialectType.MYSQL, new JdbcTemplate(ds));

        assertEquals(0, rules.size());
    }

    @Test
    public void testFullFillRiskLevel() {
        List<CheckViolation> violations = Arrays.asList(new CheckViolation(), new CheckViolation());
        List<Rule> rules = sqlCheckService.fullFillRiskLevel(Collections.emptyList(), violations);

        assertEquals(violations.size(), rules.size());
    }
}
