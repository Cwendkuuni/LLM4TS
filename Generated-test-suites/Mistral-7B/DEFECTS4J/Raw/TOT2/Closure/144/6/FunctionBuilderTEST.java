import static org.junit.Assert.*;
import com.google.javascript.rhino.jstype.FunctionBuilder;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.ObjectType;

public class FunctionBuilderTest {

  @Test
  public void testFunctionBuilderConstructor() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);
    assertNull(functionBuilder.getName());
    assertNull(functionBuilder.getSourceNode());
    assertNull(functionBuilder.getParametersNode());
    assertNull(functionBuilder.getReturnType());
    assertNull(functionBuilder.getTypeOfThis());
    assertNull(functionBuilder.getTemplateTypeName());
    assertFalse(functionBuilder.isConstructor());
    assertFalse(functionBuilder.isNativeType());
  }
}