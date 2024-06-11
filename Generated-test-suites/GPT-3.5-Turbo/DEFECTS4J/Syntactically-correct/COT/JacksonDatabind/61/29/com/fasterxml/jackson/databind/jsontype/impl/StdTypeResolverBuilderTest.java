
import org.junit.Test;

import static org.junit.Assert.*;

public class StdTypeResolverBuilderTest {

    @Test
    public void testInit() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.NAME, null);
        assertEquals(JsonTypeInfo.Id.NAME, builder._idType);
        assertNull(builder._customIdResolver);
        assertEquals("class", builder._typeProperty);
    }

    @Test
    public void testNoTypeInfoBuilder() {
        StdTypeResolverBuilder builder = StdTypeResolverBuilder.noTypeInfoBuilder();
        assertEquals(JsonTypeInfo.Id.NONE, builder._idType);
        assertNull(builder._customIdResolver);
        assertNull(builder._typeProperty);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInitWithNullIdType() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(null, null);
    }

    @Test
    public void testBuildTypeSerializerWithNoneIdType() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.NONE, null);
        assertNull(builder.buildTypeSerializer(null, null, null));
    }

    @Test(expected = IllegalStateException.class)
    public void testBuildTypeSerializerWithUnknownIncludeAsType() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.inclusion(JsonTypeInfo.As.EXTERNAL_PROPERTY);
        builder.buildTypeSerializer(null, null, null);
    }

    // Add more test cases for other public methods in StdTypeResolverBuilder class

}

