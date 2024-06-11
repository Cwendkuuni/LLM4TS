
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;
import com.oceanbase.odc.service.sqlcheck.SqlCheckService;

import java.util.ArrayList;
import java.util.Collections;

import static org.junit.Assert.assertEquals;

public class SqlCheckServiceTest {

    @InjectMocks
    SqlCheckService sqlCheckService;

    @Mock
    RuleService ruleService;

    @Mock
    EnvironmentService environmentService;

    @Mock
    ConnectionSession connectionSession;

    @Mock
    SqlCheckReq sqlCheckReq;

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCheckSessionReq() {
        when(connectionSession.getRuleSetId()).thenReturn(1L);
        when(connectionSession.getDialectType()).thenReturn(DialectType.PLSQL);
        when(ruleService.list(anyLong(), any())).thenReturn(new ArrayList<>());
        assertEquals(Collections.emptyList(), sqlCheckService.check(connectionSession, sqlCheckReq));
    }

    @Test
    public void testCheckEnvironmentIdDatabaseNameSqlsConfig() {
        when(environmentService.detail(anyLong())).thenReturn(new Environment());
        when(ruleService.list(anyLong(), any())).thenReturn(new ArrayList<>());
        assertEquals(Collections.emptyList(), sqlCheckService.check(1L, "testDatabase", new ArrayList<>(), new ConnectionConfig()));
    }

    @Test
    public void testGetRules() {
        assertEquals(Collections.emptyList(), sqlCheckService.getRules(new ArrayList<>(), DialectType.PLSQL, null));
    }

    @Test
    public void testFullFillRiskLevel() {
        assertEquals(new ArrayList<>(), sqlCheckService.fullFillRiskLevel(new ArrayList<>(), new ArrayList<>()));
    }
}
