import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.std.MapSerializer;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import java.util.HashMap;
import java.util.Map;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class AnyGetterWriterTest {

    private AnyGetterWriter anyGetterWriter;
    private BeanProperty writerProperty;
    private AnnotatedMember accessor;
    private MapSerializer serializer;
    private SerializerProvider provider;

    @Before
    public void setUp() {
        writerProperty = new BeanProperty() {
            @Override
            public String getName() {
                return "testProperty";
            }

            @Override
            public PropertyMetadata getMetadata() {
                return null;
            }

            @Override
            public AnnotatedMember getMember() {
                return null;
            }

            @Override
            public JavaType getType() {
                return null;
            }
        };
        accessor = new AnnotatedMember() {
            @Override
            public Class<?> getDeclaringClass() {
                return AnyGetterWriter.class;
            }

            @Override
            public void fixAccess() {

            }

            @Override
            public Object getValue(Object pojo) throws IllegalArgumentException {
                return new HashMap<String, String>();
            }
        };
        serializer = new MapSerializer(null, null, null, false, null, null, null, null);
        anyGetterWriter = new AnyGetterWriter(writerProperty, accessor, serializer);
        provider = new DefaultSerializerProvider.Impl();
    }

    @Test
    public void testGetAndSerialize() throws Exception {
        JsonGenerator gen = new JsonFactory().createGenerator(System.out);
        anyGetterWriter.getAndSerialize(new Object(), gen, provider);
        gen.close();
    }

    @Test
    public void testGetAndFilter() throws Exception {
        JsonGenerator gen = new JsonFactory().createGenerator(System.out);
        PropertyFilter filter = new PropertyFilter() {
            @Override
            public void serializeAsField(Object bean, JsonGenerator gen, SerializerProvider prov, PropertyWriter writer) throws Exception {

            }

            @Override
            public void serializeAsElement(Object elementValue, JsonGenerator gen, SerializerProvider prov, PropertyWriter writer) throws Exception {

            }
        };
        anyGetterWriter.getAndFilter(new Object(), gen, provider, filter);
        gen.close();
    }

    @Test
    public void testResolve() throws JsonMappingException {
        anyGetterWriter.resolve(provider);
        Assert.assertNotNull(anyGetterWriter._mapSerializer);
    }
}