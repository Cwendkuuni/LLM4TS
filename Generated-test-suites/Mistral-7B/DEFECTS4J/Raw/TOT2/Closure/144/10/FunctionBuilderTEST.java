import static org.junit.Assert.assertNotNull;
import com.google.javascript.rhino.jstype.FunctionBuilder;
import com.google.javascript.rhino.jstype.JSTypeRegistry;

public class FunctionBuilderTest {

  @Test
  public void testFunctionBuilderConstructor() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);
    assertNotNull(functionBuilder);
  }
}