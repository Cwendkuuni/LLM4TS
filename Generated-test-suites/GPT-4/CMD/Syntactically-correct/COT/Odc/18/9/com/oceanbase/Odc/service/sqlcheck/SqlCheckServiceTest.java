package com.oceanbase.odc.service.sqlcheck;

import com.oceanbase.odc.service.sqlcheck.SqlCheckService;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;
import com.oceanbase.odc.core.sql.split.OffsetString;
import com.oceanbase.odc.core.shared.constant.DialectType;
import static org.mockito.Mockito.*;
import java.util.Arrays;
import java.util.List;
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class SqlCheckServiceTest {
    @InjectMocks
    private SqlCheckService sqlCheckService;
    @Mock
    private ConnectionSession connectionSession;
    @Mock
    private ConnectionConfig connectionConfig;
    @Mock
    private SqlCheckReq sqlCheckReq;

    @Test
    public void testCheckMethodOne() {
        when(connectionSession.getDialectType()).thenReturn(DialectType.ORACLE);
        when(sqlCheckReq.getDelimiter()).thenReturn(";");
        when(sqlCheckReq.getScriptContent()).thenReturn("SELECT * FROM table;");
        List<CheckResult> checkResultList = sqlCheckService.check(connectionSession, sqlCheckReq);
        // assert and verify
    }

    @Test
    public void testCheckMethodTwo() {
        List<OffsetString> sqls = Arrays.asList(new OffsetString("SELECT * FROM table;", 0));
        List<CheckViolation> violations = sqlCheckService.check(1L, "databaseName", sqls, connectionConfig);
        // assert and verify
    }

    @Test
    public void testGetRulesMethodOne() {
        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(null, connectionSession);
        // assert and verify
    }

    @Test
    public void testGetRulesMethodTwo() {
        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(null, DialectType.ORACLE, null);
        // assert and verify
    }

    @Test
    public void testFullFillRiskLevel() {
        List<CheckViolation> violations = Arrays.asList(new CheckViolation());
        List<Rule> rules = sqlCheckService.fullFillRiskLevel(null, violations);
        // assert and verify
    }
}
