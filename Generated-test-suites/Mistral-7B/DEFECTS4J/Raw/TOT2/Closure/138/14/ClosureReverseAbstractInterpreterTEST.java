import static org.junit.Assert.assertNotNull;

public class ClosureReverseAbstractInterpreterTest {

  @Test
  public void testConstructor() {
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        null, null);
    assertNotNull(interpreter);
  }
}