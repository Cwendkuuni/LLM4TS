import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

public class ArrowTypeTest {

  @Test
  public void testArrowTypeConstruction() {
    JSTypeRegistry registry = mock(JSTypeRegistry.class);
    Node parameters = mock(Node.class);
    JSType returnType = mock(JSType.class);

    ArrowType arrowType = new ArrowType(registry, parameters, returnType);

    assertEquals(registry, arrowType.getRegistry());
    assertEquals(parameters, arrowType.getParameters());
    assertEquals(returnType, arrowType.getReturnType());
  }

  @Test
  public void testArrowTypeConstructionWithNullRegistry() {
    Node parameters = mock(Node.class);
    JSType returnType = mock(JSType.class);

    ArrowType arrowType = new ArrowType(null, parameters, returnType);

    assertNull(arrowType);
  }

  @Test
  public void testArrowTypeConstructionWithNullParameters() {
    JSTypeRegistry registry = mock(JSTypeRegistry.class);
    JSType returnType = mock(JSType.class);

    ArrowType arrowType = new ArrowType(registry, null, returnType);

    assertNull(arrowType);
  }
}