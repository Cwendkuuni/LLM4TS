import static org.junit.Assert.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class ArrowTypeTest {

  @Mock
  JSTypeRegistry mockRegistry;

  private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
  private final PrintStream originalOut = System.out;

  @Before
  public void setUpStreams() {
    System.setOut(new PrintStream(outContent));
  }

  @After
  public void restoreStreams() {
    System.setOut(originalOut);
  }

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void testIsSubtype_ReturnTrue() {
    Node parameters = new Node();
    JSType returnType = new JSType(mockRegistry);
    ArrowType arrowType1 = new ArrowType(mockRegistry, parameters, returnType);
    ArrowType arrowType2 = new ArrowType(mockRegistry, parameters, returnType);
    assertTrue(arrowType1.isSubtype(arrowType2));
  }

  @Test
  public void testIsSubtype_ReturnFalse() {
    Node parameters = new Node();
    JSType returnType1 = new JSType(mockRegistry);
    JSType returnType2 = new JSType(mockRegistry);
    ArrowType arrowType1 = new ArrowType(mockRegistry, parameters, returnType1);
    ArrowType arrowType2 = new ArrowType(mockRegistry, parameters, returnType2);
    assertFalse(arrowType1.isSubtype(arrowType2));
  }

  @Test
  public void testHasEqualParameters_ReturnTrue() {
    Node parameters = new Node();
    ArrowType arrowType1 = new ArrowType(mockRegistry, parameters, null);
    ArrowType arrowType2 = new ArrowType(mockRegistry, parameters, null);
    assertTrue(arrowType1.hasEqualParameters(arrowType2));
  }

  @Test
  public void testHasEqualParameters_ReturnFalse() {
    Node parameters1 = new Node();
    Node parameters2 = new Node();
    ArrowType arrowType1 = new ArrowType(mockRegistry, parameters1, null);
    ArrowType arrowType2 = new ArrowType(mockRegistry, parameters2, null);
    assertFalse(arrowType1.hasEqualParameters(arrowType2));
  }

  @Test
  public void testIsEquivalentTo_ReturnTrue() {
    Node parameters = new Node();
    JSType returnType = new JSType(mockRegistry);
    ArrowType arrowType1 = new ArrowType(mockRegistry, parameters, returnType);
    ArrowType arrowType2 = new ArrowType(mockRegistry, parameters, returnType);
    assertTrue(arrowType1.isEquivalentTo(arrowType2));
  }

  @Test
  public void testIsEquivalentTo_ReturnFalse() {
    Node parameters = new Node();
    JSType returnType1 = new JSType(mockRegistry);
    JSType returnType2 = new JSType(mockRegistry);
    ArrowType arrowType1 = new ArrowType(mockRegistry, parameters, returnType1);
    ArrowType arrowType2 = new ArrowType(mockRegistry, parameters, returnType2);
    assertFalse(arrowType1.isEquivalentTo(arrowType2));
  }

  @Test
  public void testHashCode() {
    Node parameters = new Node();
    JSType returnType = new JSType(mockRegistry);
    ArrowType arrowType = new ArrowType(mockRegistry, parameters, returnType);
    assertEquals(parameters.hashCode() + returnType.hashCode(), arrowType.hashCode());
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetLeastSupertype() {
    Node parameters = new Node();
    JSType returnType = new JSType(mockRegistry);
    ArrowType arrowType = new ArrowType(mockRegistry, parameters, returnType);
    arrowType.getLeastSupertype(mockRegistry);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetGreatestSubtype() {
    Node parameters = new Node();
    JSType returnType = new JSType(mockRegistry);
    ArrowType arrowType = new ArrowType(mockRegistry, parameters, returnType);
    arrowType.getGreatestSubtype(mockRegistry);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testTestForEquality() {
    Node parameters = new Node();
    JSType returnType = new JSType(mockRegistry);
    ArrowType arrowType = new ArrowType(mockRegistry, parameters, returnType);
    arrowType.testForEquality(mockRegistry);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testVisit() {
    Node parameters = new Node();
    JSType returnType = new JSType(mockRegistry);
    ArrowType arrowType = new ArrowType(mockRegistry, parameters, returnType);
    arrowType.visit(mockRegistry);
  }

  @Test
  public void testGetPossibleToBooleanOutcomes() {
    Node parameters = new Node();
    JSType returnType = new JSType(mockRegistry);
    ArrowType arrowType = new ArrowType(mockRegistry, parameters, returnType);
    assertEquals(BooleanLiteralSet.TRUE, arrowType.getPossibleToBooleanOutcomes());
  }

  @Test
  public void testResolveInternal() {
    Node parameters = new Node();
    JSType returnType = new JSType(mockRegistry);
    ArrowType arrowType = new ArrowType(mockRegistry, parameters, returnType);
    arrowType.resolveInternal(mockRegistry, null);
    // Verify that the resolve method was called for the returnType
    // and each parameter's JSType
    assertNotNull(returnType.getResolution());
    assertNotNull(parameters.getFirstChild().getJSType().getResolution());
  }

  @Test
  public void testHasUnknownParamsOrReturn_ReturnTrue() {
    Node parameters = new Node();
    ArrowType arrowType = new ArrowType(mockRegistry, parameters, null);
    assertTrue(arrowType.hasUnknownParamsOrReturn());
  }

  @Test
  public void testHasUnknownParamsOrReturn_ReturnFalse() {
    Node parameters = new Node();
    JSType returnType = new JSType(mockRegistry);
    ArrowType arrowType = new ArrowType(mockRegistry, parameters, returnType);
    assertFalse(arrowType.hasUnknownParamsOrReturn());
  }

  @Test
  public void testToStringHelper() {
    Node parameters = new Node();
    JSType returnType = new JSType(mockRegistry);
    ArrowType arrowType = new ArrowType(mockRegistry, parameters, returnType);
    assertEquals(super.toString(), arrowType.toStringHelper(false));
  }
}