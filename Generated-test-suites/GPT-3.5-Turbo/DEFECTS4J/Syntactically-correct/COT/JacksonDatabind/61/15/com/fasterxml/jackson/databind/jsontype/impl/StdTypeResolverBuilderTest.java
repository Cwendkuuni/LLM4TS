import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.databind.jsontype.impl.StdTypeResolverBuilder;
import com.fasterxml.jackson.databind.*;

public class StdTypeResolverBuilderTest {

    @Test
    public void testInit() {
        StdTypeResolverBuilder resolver = new StdTypeResolverBuilder();
        resolver.init(JsonTypeInfo.Id.CLASS, null);
        assertNotNull(resolver._idType);
        assertNotNull(resolver._typeProperty);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInitWithNullIdType() {
        StdTypeResolverBuilder resolver = new StdTypeResolverBuilder();
        resolver.init(null, null);
    }

    @Test
    public void testNoTypeInfoBuilder() {
        StdTypeResolverBuilder resolver = StdTypeResolverBuilder.noTypeInfoBuilder();
        assertNotNull(resolver._idType);
        assertNotNull(resolver._typeProperty);
    }

    @Test
    public void testBuildTypeSerializer() {
        StdTypeResolverBuilder resolver = new StdTypeResolverBuilder();
        resolver.init(JsonTypeInfo.Id.CLASS, null);
        TypeSerializer serializer = resolver.buildTypeSerializer(null, null, null);
        assertNotNull(serializer);
    }

    @Test
    public void testBuildTypeDeserializer() {
        StdTypeResolverBuilder resolver = new StdTypeResolverBuilder();
        resolver.init(JsonTypeInfo.Id.CLASS, null);
        TypeDeserializer deserializer = resolver.buildTypeDeserializer(null, null, null);
        assertNotNull(deserializer);
    }

    @Test
    public void testInclusion() {
        StdTypeResolverBuilder resolver = new StdTypeResolverBuilder();
        resolver.inclusion(JsonTypeInfo.As.WRAPPER_ARRAY);
        assertEquals(JsonTypeInfo.As.WRAPPER_ARRAY, resolver._includeAs);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInclusionWithNull() {
        StdTypeResolverBuilder resolver = new StdTypeResolverBuilder();
        resolver.inclusion(null);
    }

    @Test
    public void testTypeProperty() {
        StdTypeResolverBuilder resolver = new StdTypeResolverBuilder();
        String typeIdPropName = "type";
        resolver.typeProperty(typeIdPropName);
        assertEquals(typeIdPropName, resolver._typeProperty);
    }

    @Test
    public void testDefaultImpl() {
        StdTypeResolverBuilder resolver = new StdTypeResolverBuilder();
        Class<?> defaultImpl = String.class;
        resolver.defaultImpl(defaultImpl);
        assertEquals(defaultImpl, resolver._defaultImpl);
    }

    @Test
    public void testTypeIdVisibility() {
        StdTypeResolverBuilder resolver = new StdTypeResolverBuilder();
        resolver.typeIdVisibility(true);
        assertTrue(resolver._typeIdVisible);
    }


}