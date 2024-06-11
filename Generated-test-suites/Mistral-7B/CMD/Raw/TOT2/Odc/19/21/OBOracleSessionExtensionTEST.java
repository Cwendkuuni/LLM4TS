import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.pf4j.Extension;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension;

@Extension
public class OBOracleSessionExtensionTest {
    private OBOracleSessionExtension obOracleSessionExtension;
    private OceanBaseConnection connectionMock;

    @BeforeEach
    public void setup() {
        connectionMock = Mockito.mock(OceanBaseConnection.class);
        obOracleSessionExtension = new OBOracleSessionExtension();
        obOracleSessionExtension.setConnection(connectionMock);
    }

    @Test
    public void testKillQuery_ValidConnectionId_Success() {
        String connectionId = "123456";
        Mockito.doNothing().when(connectionMock).execute(ArgumentMatchers.contains("KILL QUERY " + connectionId));

        obOracleSessionExtension.killQuery(connectionMock, connectionId);

        Mockito.verify(connectionMock, Mockito.times(1)).execute(ArgumentMatchers.contains("KILL QUERY " + connectionId));
    }

    @Test
    public void testKillQuery_InvalidConnectionId_Exception() {
        String connectionId = null;

        Assertions.assertThrows(NullPointerException.class, () -> obOracleSessionExtension.killQuery(connectionMock, connectionId));
    }
}