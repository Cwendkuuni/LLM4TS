import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import org.junit.Before;
import org.junit.Test;
import java.util.Map;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class RecordTypeBuilderTest {

  private JSTypeRegistry registryMock;
  private RecordTypeBuilder recordTypeBuilder;

  @Before
  public void setUp() {
    registryMock = mock(JSTypeRegistry.class);
    recordTypeBuilder = new RecordTypeBuilder(registryMock);
  }

  @Test
  public void testAddProperty() {
    JSType typeMock = mock(JSType.class);
    Node propertyNodeMock = mock(Node.class);

    recordTypeBuilder.addProperty("property1", typeMock, propertyNodeMock);

    Map<String, RecordTypeBuilder.RecordProperty> properties = recordTypeBuilder.getProperties();
    assertEquals(properties.size(), 1);
    assertEquals(properties.get("property1").getType(), typeMock);
    assertEquals(properties.get("property1").getPropertyNode(), propertyNodeMock);
  }

  @Test
  public void testBuild_EmptyRecord() {
    JSType objectTypeMock = mock(JSType.class);

    when(registryMock.getNativeObjectType(JSTypeNative.OBJECT_TYPE)).thenReturn(objectTypeMock);

    JSType result = recordTypeBuilder.build();

    assertEquals(result, objectTypeMock);
  }

  @Test
  public void testBuild_NonEmptyRecord() {
    Map<String, RecordTypeBuilder.RecordProperty> properties = new HashMap<>();
    JSType propertyTypeMock = mock(JSType.class);
    Node propertyNodeMock = mock(Node.class);
    properties.put("property1", new RecordTypeBuilder.RecordProperty(propertyTypeMock, propertyNodeMock));

    when(registryMock.getNativeObjectType(JSTypeNative.OBJECT_TYPE)).thenReturn(objectTypeMock);

    JSType result = recordTypeBuilder.build();

    assertEquals(result.getClass(), RecordType.class);
    assertEquals(((RecordType)result).getProperties(), properties);
  }
}