import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class OnlineSchemaChangeParametersTest {

  @Test
  public void testIsContinueOnError() {
    OnlineSchemaChangeParameters obj = new OnlineSchemaChangeParameters();
    obj.setErrorStrategy(TaskErrorStrategy.CONTINUE);
    assertEquals(true, obj.isContinueOnError());
  }
}