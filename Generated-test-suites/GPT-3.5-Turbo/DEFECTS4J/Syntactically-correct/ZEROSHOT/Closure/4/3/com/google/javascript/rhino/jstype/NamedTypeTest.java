import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.Mockito;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.Node;

public class NamedTypeTest {

  @Test
  public void testGetReferencedType() {
    JSTypeRegistry registryMock = Mockito.mock(JSTypeRegistry.class);
    String reference = "testReference";
    String sourceName = "testSourceName";
    int lineno = 1;
    int charno = 2;
    NamedType namedType = new NamedType(registryMock, reference, sourceName, lineno, charno);

    JSType referencedTypeMock = Mockito.mock(JSType.class);
    Mockito.when(registryMock.getType(reference)).thenReturn(referencedTypeMock);

    assertEquals(referencedTypeMock, namedType.getReferencedType());
    Mockito.verify(registryMock).getType(reference);
  }

  @Test
  public void testGetReferenceName() {
    JSTypeRegistry registryMock = Mockito.mock(JSTypeRegistry.class);
    String reference = "testReference";
    String sourceName = "testSourceName";
    int lineno = 1;
    int charno = 2;
    NamedType namedType = new NamedType(registryMock, reference, sourceName, lineno, charno);

    assertEquals(reference, namedType.getReferenceName());
  }

  @Test
  public void testHasReferenceName() {
    JSTypeRegistry registryMock = Mockito.mock(JSTypeRegistry.class);
    String reference = "testReference";
    String sourceName = "testSourceName";
    int lineno = 1;
    int charno = 2;
    NamedType namedType = new NamedType(registryMock, reference, sourceName, lineno, charno);

    assertTrue(namedType.hasReferenceName());
  }

  @Test
  public void testIsNamedType() {
    JSTypeRegistry registryMock = Mockito.mock(JSTypeRegistry.class);
    String reference = "testReference";
    String sourceName = "testSourceName";
    int lineno = 1;
    int charno = 2;
    NamedType namedType = new NamedType(registryMock, reference, sourceName, lineno, charno);

    assertTrue(namedType.isNamedType());
  }

  @Test
  public void testIsNominalType() {
    JSTypeRegistry registryMock = Mockito.mock(JSTypeRegistry.class);
    String reference = "testReference";
    String sourceName = "testSourceName";
    int lineno = 1;
    int charno = 2;
    NamedType namedType = new NamedType(registryMock, reference, sourceName, lineno, charno);

    assertTrue(namedType.isNominalType());
  }

  @Test
  public void testResolveInternal() {
    ErrorReporter errorReporterMock = Mockito.mock(ErrorReporter.class);
    String reference = "testReference";
    String sourceName = "testSourceName";
    int lineno = 1;
    int charno = 2;
    JSTypeRegistry registryMock = Mockito.mock(JSTypeRegistry.class);
    Mockito.when(registryMock.isLastGeneration()).thenReturn(true);
    JSType referencedTypeMock = Mockito.mock(JSType.class);
    Mockito.when(registryMock.getType(reference)).thenReturn(referencedTypeMock);
    StaticScope<JSType> enclosingMock = Mockito.mock(StaticScope.class);

    NamedType namedType = new NamedType(registryMock, reference, sourceName, lineno, charno);
    namedType.resolveInternal(errorReporterMock, enclosingMock);

    assertEquals(referencedTypeMock, namedType.getReferencedType());
    Mockito.verify(registryMock).getType(reference);
    Mockito.verify(referencedTypeMock).isUnknownType();
    Mockito.verify(registryMock).isLastGeneration();
  }
}