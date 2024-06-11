import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

public class XmlSerializerProviderTest {

    private XmlSerializerProvider xmlSerializerProvider;
    private XmlRootNameLookup rootNameLookup;
    private ToXmlGenerator xmlGeneratorMock;

    @Before
    public void setup() {
        rootNameLookup = mock(XmlRootNameLookup.class);
        xmlSerializerProvider = new XmlSerializerProvider(rootNameLookup);
        xmlGeneratorMock = mock(ToXmlGenerator.class);
    }

    @Test
    public void serializeValue_WithNullValue_ShouldSerializeNull() throws Exception {
        // Arrange
        Object value = null;
        DefaultSerializerProvider defaultSerializerProviderMock = mock(DefaultSerializerProvider.class);
        doCallRealMethod().when(xmlSerializerProvider).serializeValue(any(JsonGenerator.class), any(Object.class));
        xmlSerializerProvider = spy(xmlSerializerProvider);
        
        // Act
        xmlSerializerProvider.serializeValue(xmlGeneratorMock, value);
        
        // Assert
        verify(xmlGeneratorMock).writeStartObject();
        verify(xmlSerizlierProvider).serializeValue(xmlGeneratorMock, null); 
        verify(xmlGeneratorMock).writeEndObject();
    }

    @Test
    public void serializeValue_WithNonNullObjectValue_ShouldSerializeObject() throws Exception {
        // Arrange
        Object value = new Object();
        DefaultSerializerProvider defaultSerializerProviderMock = mock(DefaultSerializerProvider.class);
        doCallRealMethod().when(xmlSerializerProvider).serializeValue(any(JsonGenerator.class), any(Object.class));
        xmlSerializerProvider = spy(xmlSerializerProvider);

        // Act
        xmlSerializerProvider.serializeValue(xmlGeneratorMock, value);

        // Assert
        verify(xmlGeneratorMock).writeObjectName(any(QName.class));
        verify(defaultSerializerProviderMock).serializeValue(xmlGeneratorMock, value);
    }

    @Test
    public void serializeValue_WithIndexedType_ShouldStartRootArrayAndSerialize() throws Exception {
        // Arrange
        Object value = new Object();
        DefaultSerializerProvider defaultSerializerProviderMock = mock(DefaultSerializerProvider.class);
        doCallRealMethod().when(xmlSerializerProvider).serializeValue(any(JsonGenerator.class), any(Object.class));
        xmlSerializerProvider = spy(xmlSerializerProvider);
        when(TypeUtil.isIndexedType(any(Class.class))).thenReturn(true);

        // Act
        xmlSerializerProvider.serializeValue(xmlGeneratorMock, value);

        // Assert
        verify(xmlGeneratorMock).writeStartObject();
        verify(xmlGeneratorMock).writeFieldName("item");
        verify(xmlGeneratorMock).writeEndObject();
        verify(defaultSerializerProviderMock).serializeValue(xmlGeneratorMock, value);
    }

    @Test
    public void serializeValue_WithNullObjectValueAndRootNameFromConfig_ShouldSerializeNullWithRootNameFromConfig() throws Exception {
        // Arrange
        Object value = null;
        DefaultSerializerProvider defaultSerializerProviderMock = mock(DefaultSerializerProvider.class);
        doCallRealMethod().when(xmlSerializerProvider).serializeValue(any(JsonGenerator.class), any(Object.class));
        doCallRealMethod().when(xmlSerializerProvider).serializeValue(any(JsonGenerator.class), any(Object.class), any(JavaType.class), any(JsonSerializer.class));
        xmlSerializerProvider = spy(xmlSerializerProvider);
        xmlSerializerProvider._config.setFullRootName(new PropertyName("RootName"));

        // Act
        xmlSerializerProvider.serializeValue(xmlGeneratorMock, value, null, null);

        // Assert
        verify(xmlGeneratorMock).initWithRootName(any(QName.class));
        verify(defaultSerializerProviderMock).serializeValue(xmlGeneratorMock, value);
    }

    @Test
    public void serializeValue_WithNonNullObjectValueAndRootNameFromConfig_ShouldSerializeObjectWithRootNameFromConfig() throws Exception {
        // Arrange
        Object value = new Object();
        DefaultSerializerProvider defaultSerializerProviderMock = mock(DefaultSerializerProvider.class);
        doCallRealMethod().when(xmlSerializerProvider).serializeValue(any(JsonGenerator.class), any(Object.class));
        doCallRealMethod().when(xmlSerializerProvider).serializeValue(any(JsonGenerator.class), any(Object.class), any(JavaType.class), any(JsonSerializer.class));
        xmlSerializerProvider = spy(xmlSerializerProvider);
        xmlSerializerProvider._config.setFullRootName(new PropertyName("RootName"));

        // Act
        xmlSerializerProvider.serializeValue(xmlGeneratorMock, value, null, null);

        // Assert
        verify(xmlGeneratorMock).initWithRootName(any(QName.class));
        verify(defaultSerializerProviderMock).serializeValue(xmlGeneratorMock, value);
    }

}