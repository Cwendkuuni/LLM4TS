
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class SqlCheckServiceTest {

    private SqlCheckService sqlCheckService;

    @Before
    public void setUp() {
        sqlCheckService = new SqlCheckService();
    }

    @Test
    public void testCheckWithValidSessionAndRequest() {
        ConnectionSession session = Mockito.mock(ConnectionSession.class);
        SqlCheckReq req = new SqlCheckReq(); // Initialize with valid data
        List<CheckResult> results = sqlCheckService.check(session, req);

        assertNotNull(results);
        assertTrue(results.isEmpty());
    }

    @Test
    public void testCheckWithNullRuleSetId() {
        ConnectionSession session = Mockito.mock(ConnectionSession.class);
        SqlCheckReq req = new SqlCheckReq(); // Initialize with valid data
        Mockito.when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(null);
        List<CheckResult> results = sqlCheckService.check(session, req);

        assertNotNull(results);
        assertTrue(results.isEmpty());
    }

    @Test
    public void testCheckWithNoRulesAvailable() {
        ConnectionSession session = Mockito.mock(ConnectionSession.class);
        SqlCheckReq req = new SqlCheckReq(); // Initialize with valid data
        Mockito.when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(123L);
        Mockito.when(ruleService.list(123L, QueryRuleMetadataParams.builder().build())).thenReturn(Collections.emptyList());
        List<CheckResult> results = sqlCheckService.check(session, req);

        assertNotNull(results);
        assertTrue(results.isEmpty());
    }

    // Additional test cases for other methods can be added here

}

