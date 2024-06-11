import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.ser.SerializerFactory;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.dataformat.xml.ser.XmlSerializerProvider;
import com.fasterxml.jackson.dataformat.xml.util.StaxUtil;
import com.fasterxml.jackson.dataformat.xml.util.TypeUtil;
import com.fasterxml.jackson.dataformat.xml.util.XmlRootNameLookup;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;

import java.io.IOException;

import static org.mockito.Mockito.*;

public class XmlSerializerProviderTest {

    @Mock
    private SerializationConfig config;

    @Mock
    private SerializerFactory factory;

    @Mock
    private XmlRootNameLookup rootNameLookup;

    private XmlSerializerProvider provider;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        provider = new XmlSerializerProvider(rootNameLookup);
    }

    @Test
    public void copy_ShouldReturnNewXmlSerializerProviderWithSameRootNameLookup() {
        XmlSerializerProvider copy = provider.copy();
        assertNotSame(provider, copy);
        assertEquals(provider.getRootNameLookup(), copy.getRootNameLookup());
    }

    @Test
    public void createInstance_ShouldReturnNewXmlSerializerProviderWithSameRootNameLookup() {
        XmlSerializerProvider instance = provider.createInstance(config, factory);
        assertNotSame(provider, instance);
        assertEquals(provider.getRootNameLookup(), instance.getRootNameLookup());
    }

    @Test
    public void serializeValue_WhenValueIsNull_ShouldSerializeXmlNull() throws IOException {
        XmlSerializerProvider spyProvider = spy(provider);
        XmlGeneratorStub generatorStub = new XmlGeneratorStub();
        when(spyProvider._asXmlGenerator(any())).thenReturn(generatorStub);
        when(spyProvider._rootNameFromConfig()).thenReturn(null);

        spyProvider.serializeValue(generatorStub, null);

        verify(generatorStub).writeStartObject();
        verify(generatorStub).writeFieldName("item");
        verify(generatorStub).writeEndObject();
    }

    @Test
    public void serializeValue_WhenValueIsNullAndRootNameFromConfig_ShouldSerializeXmlNullWithRootNameFromConfig()
            throws IOException {
        XmlSerializerProvider spyProvider = spy(provider);
        XmlGeneratorStub generatorStub = new XmlGeneratorStub();
        when(spyProvider._asXmlGenerator(any())).thenReturn(generatorStub);
        QName rootName = new QName("null");
        when(spyProvider._rootNameFromConfig()).thenReturn(rootName);

        spyProvider.serializeValue(generatorStub, null);

        verify(generatorStub).writeStartObject();
        verify(generatorStub).writeFieldName("item");
        verify(generatorStub).writeEndObject();
    }

    @Test
    public void serializeValue_WhenValueNotNull_ShouldSerializeValueWithRootName() throws IOException {
        XmlSerializerProvider spyProvider = spy(provider);
        XmlGeneratorStub generatorStub = new XmlGeneratorStub();
        when(spyProvider._asXmlGenerator(any())).thenReturn(generatorStub);
        when(spyProvider._rootNameFromConfig()).thenReturn(null);
        when(spyProvider.findTypedValueSerializer(any(), anyBoolean(), any())).thenReturn(mock(JsonSerializer.class));

        spyProvider.serializeValue(generatorStub, new Object());

        verify(generatorStub, never()).writeStartObject();
        verify(generatorStub, never()).writeFieldName("item");
        verify(generatorStub, never()).writeEndObject();
    }

    private static class XmlGeneratorStub extends ToXmlGenerator {
        public XmlGeneratorStub() {
            super(new JsonStringEncoder());
        }

        @Override
        public boolean inRoot() {
            return true;
        }

        @Override
        protected void _serializeXmlNull(JsonGenerator jgen) throws IOException {
            // Do nothing
        }

        @Override
        protected void _startRootArray(ToXmlGenerator xgen, QName rootName) throws IOException {
            // Do nothing
        }

        @Override
        protected void _initWithRootName(ToXmlGenerator xgen, QName rootName) throws IOException {
            // Do nothing
        }
    }
}