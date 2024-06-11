import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule;
import com.oceanbase.odc.service.sqlcheck.model.OffsetString;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class SqlCheckServiceTest {

    @InjectMocks
    private SqlCheckService sqlCheckService;
    @Mock
    private RuleService ruleService;
    @Mock
    private EnvironmentService environmentService;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCheckForConnectionSession() {
        List<Rule> rules = new ArrayList<>();
        ConnectionSession connectionSession = Mockito.mock(ConnectionSession.class);
        SqlCheckReq req = Mockito.mock(SqlCheckReq.class);
        when(ruleService.list(any(Long.class), any())).thenReturn(rules);
        assertEquals(new ArrayList<>(), sqlCheckService.check(connectionSession, req));
    }

    @Test
    public void testCheckForEnvironmentId() {
        List<OffsetString> sqls = new ArrayList<>();
        ConnectionConfig config = Mockito.mock(ConnectionConfig.class);
        Environment env = Mockito.mock(Environment.class);
        when(environmentService.detail(any(Long.class))).thenReturn(env);
        assertEquals(new ArrayList<>(), sqlCheckService.check(1L, "", sqls, config));
    }

    @Test
    public void testGetRulesForSession(){
        List<Rule> rules = new ArrayList<>();
        ConnectionSession session = Mockito.mock(ConnectionSession.class);
        assertEquals(new ArrayList<>(), sqlCheckService.getRules(rules, session));
    }
}