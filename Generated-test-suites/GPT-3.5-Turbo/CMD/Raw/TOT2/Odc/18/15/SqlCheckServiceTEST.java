import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.junit.Test;

public class SqlCheckServiceTest {

    @Test
    public void testCheckWithValidSessionAndReq() {
        SqlCheckService sqlCheckService = new SqlCheckService();
        ConnectionSession session = mock(ConnectionSession.class);
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);

        SqlCheckReq req = new SqlCheckReq();
        req.setScriptContent("SELECT * FROM table");

        List<CheckResult> actual = sqlCheckService.check(session, req);
        assertNotNull(actual);
        assertEquals(0, actual.size());
    }

    @Test
    public void testCheckWithInvalidSession() {
        SqlCheckService sqlCheckService = new SqlCheckService();
        ConnectionSession session = mock(ConnectionSession.class);
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(null);

        SqlCheckReq req = new SqlCheckReq();
        req.setScriptContent("SELECT * FROM table");

        List<CheckResult> actual = sqlCheckService.check(session, req);
        assertNotNull(actual);
        assertEquals(0, actual.size());
    }

    @Test
    public void testCheckWithEmptySqls() {
        SqlCheckService sqlCheckService = new SqlCheckService();
        Long environmentId = 1L;
        String databaseName = "test_db";
        List<OffsetString> sqls = Collections.emptyList();
        ConnectionConfig config = new ConnectionConfig();

        List<CheckViolation> actual = sqlCheckService.check(environmentId, databaseName, sqls, config);
        assertNotNull(actual);
        assertEquals(0, actual.size());
    }
}