import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.rhino.jstype.FunctionBuilder;
import com.google.javascript.rhino.jstype.JSTypeRegistry;

public class FunctionBuilderTest {

  @Test
  public void testFunctionBuilderConstruction() {
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