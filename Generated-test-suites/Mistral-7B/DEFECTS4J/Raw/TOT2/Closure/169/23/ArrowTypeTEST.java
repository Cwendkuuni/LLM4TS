import static org.junit.Assert.assertEquals;

public class ArrowTypeTest {
  @Test
  public void testArrowTypeConstruction() {
    JSTypeRegistry registry = mock(JSTypeRegistry.class);
    Node parameters = mock(Node.class);
    JSType returnType = mock(JSType.class);

    ArrowType arrowType = new ArrowType(registry, parameters, returnType);

    assertEquals(registry, arrowType.getTypeRegistry());
    assertEquals(parameters, arrowType.getParameters());
    assertEquals(returnType, arrowType.getReturnType());
    assertEquals(false, arrowType.isReturnTypeInferred());
  }
}