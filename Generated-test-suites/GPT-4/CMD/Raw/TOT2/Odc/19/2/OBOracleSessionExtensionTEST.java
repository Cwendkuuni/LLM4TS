import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class OBOracleSessionExtensionTest {
   @InjectMocks 
   private OBOracleSessionExtension obOracleSessionExtension;

   @Mock
   private Connection mockConnection;
   
   @Before  
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }
   
   @Test
   public void testKillQuery() {
      String testConnectionId = "testId";
      obOracleSessionExtension.killQuery(mockConnection, testConnectionId);
   }

   @Test
   public void testSwitchSchema() {
      String testSchema = "testSchema";
      obOracleSessionExtension.switchSchema(mockConnection, testSchema);
   }

   @Test
   public void testGetCurrentSchema() {
      String testSchema = "testSchema";
      String returnedSchema = obOracleSessionExtension.getCurrentSchema(mockConnection);
      assertEquals(testSchema, returnedSchema);
   }

   @Test
   public void testGetConnectionId() {
      String testConnectionId = "testId";
      String returnedId = obOracleSessionExtension.getConnectionId(mockConnection);
      assertEquals(testConnectionId, returnedId);
   }
}