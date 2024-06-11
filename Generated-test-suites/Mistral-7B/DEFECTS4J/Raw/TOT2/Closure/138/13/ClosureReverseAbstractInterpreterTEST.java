import org.junit.Test;
import static org.junit.Assert.*;

public class ClosureReverseAbstractInterpreterTest {

  @Test
  public void testConstructor() {
    CodingConvention convention = new CodingConvention();
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(convention, typeRegistry);
    assertNotNull(interpreter);
  }
}