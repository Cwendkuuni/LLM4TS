import org.junit.Test;
import static org.junit.Assert.*;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;

public class OnlineSchemaChangeParametersTest {
  
  @Test
  public void testIsContinueOnError() {
    OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
    // set values, perform actions
    // run asserts
  }

  @Test
  public void testGenerateSubTaskParameters() {
    OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
    ConnectionConfig connConfig = new ConnectionConfig();
    // set values, perform actions
    // run asserts
  }

  // And so on for all methods and constructors...
}