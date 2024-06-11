import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.util.Collection;
import java.util.ArrayList;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;
import com.fasterxml.jackson.databind.jsontype.impl.StdTypeResolverBuilder;

public class StdTypeResolverBuilderTest {
    
    private StdTypeResolverBuilder stdTypeResolverBuilder;
    
    private ObjectMapper mapper;
    
    private JavaType baseType;
    
    private Collection<NamedType> subtypes;
    
    @Before
    public void setup() {
        stdTypeResolverBuilder = new StdTypeResolverBuilder();
        mapper = new ObjectMapper();
        baseType = mapper.constructType(String.class);
        subtypes = new ArrayList<>();
    }
    
    @Test
    public void testInit() {
        TypeResolverBuilder<StdTypeResolverBuilder> actual = stdTypeResolverBuilder.init(JsonTypeInfo.Id.NAME, null);
        
        Assert.assertEquals(stdTypeResolverBuilder, actual);
    }
        
    @Test(expected = IllegalArgumentException.class)
    public void testInit_whenIdTypeIsNull_thenThrowIllegalArgumentException() {
        stdTypeResolverBuilder.init(null, null);
    }
    
    @Test
    public void testBuildTypeSerializer_whenIdTypeIsNone_thenReturnNull() {
        stdTypeResolverBuilder.init(JsonTypeInfo.Id.NONE, null);
        TypeSerializer actual = stdTypeResolverBuilder.buildTypeSerializer(null, baseType, subtypes);
        
        Assert.assertNull(actual);
    }
    
    @Test
    public void testBuildTypeDeserializer_whenIdTypeIsNone_thenReturnNull() {
        stdTypeResolverBuilder.init(JsonTypeInfo.Id.NONE, null);
        TypeDeserializer actual = stdTypeResolverBuilder.buildTypeDeserializer(null, baseType, subtypes);
        
        Assert.assertNull(actual);
    }
    
    @Test
    public void testBuildTypeSerializer_whenIncludeAsIsWrapperArray_thenReturnAsArrayTypeSerializer() {
        stdTypeResolverBuilder.inclusion(JsonTypeInfo.As.WRAPPER_ARRAY);
        TypeSerializer actual = stdTypeResolverBuilder.buildTypeSerializer(null, baseType, subtypes);
        
        Assert.assertNotNull(actual);
        Assert.assertTrue(actual instanceof AsArrayTypeSerializer);
    }
    
    @Test
    public void testBuildTypeSerializer_whenIncludeAsIsProperty_thenReturnAsPropertyTypeSerializer() {
        stdTypeResolverBuilder.inclusion(JsonTypeInfo.As.PROPERTY).typeProperty("type");
        TypeSerializer actual = stdTypeResolverBuilder.buildTypeSerializer(null, baseType, subtypes);
        
        Assert.assertNotNull(actual);
        Assert.assertTrue(actual instanceof AsPropertyTypeSerializer);
    }
    
    @Test
    public void testBuildTypeSerializer_whenIncludeAsIsWrapperObject_thenReturnAsWrapperTypeSerializer() {
        stdTypeResolverBuilder.inclusion(JsonTypeInfo.As.WRAPPER_OBJECT);
        TypeSerializer actual = stdTypeResolverBuilder.buildTypeSerializer(null, baseType, subtypes);
        
        Assert.assertNotNull(actual);
        Assert.assertTrue(actual instanceof AsWrapperTypeSerializer);
    }
    
    @Test
    public void testBuildTypeSerializer_whenIncludeAsIsExternalProperty_thenReturnAsExternalTypeSerializer() {
        stdTypeResolverBuilder.inclusion(JsonTypeInfo.As.EXTERNAL_PROPERTY).typeProperty("type");
        TypeSerializer actual = stdTypeResolverBuilder.buildTypeSerializer(null, baseType, subtypes);
        
        Assert.assertNotNull(actual);
        Assert.assertTrue(actual instanceof AsExternalTypeSerializer);
    }
    
    @Test
    public void testBuildTypeSerializer_whenIncludeAsIsExistingProperty_thenReturnAsExistingPropertyTypeSerializer() {
        stdTypeResolverBuilder.inclusion(JsonTypeInfo.As.EXISTING_PROPERTY).typeProperty("type");
        TypeSerializer actual = stdTypeResolverBuilder.buildTypeSerializer(null, baseType, subtypes);
        
        Assert.assertNotNull(actual);
        Assert.assertTrue(actual instanceof AsExistingPropertyTypeSerializer);
    }
    
    @Test(expected = IllegalStateException.class)
    public void testBuildTypeSerializer_whenIncludeAsIsInvalid_thenThrowIllegalStateException() {
        stdTypeResolverBuilder.inclusion(null);
        stdTypeResolverBuilder.buildTypeSerializer(null, baseType, subtypes);
    }
    
    @Test
    public void testBuildTypeDeserializer_whenIncludeAsIsWrapperArray_thenReturnAsArrayTypeDeserializer() {
        stdTypeResolverBuilder.inclusion(JsonTypeInfo.As.WRAPPER_ARRAY);
        TypeDeserializer actual = stdTypeResolverBuilder.buildTypeDeserializer(null, baseType, subtypes);
        
        Assert.assertNotNull(actual);
        Assert.assertTrue(actual instanceof AsArrayTypeDeserializer);
    }
    
    @Test
    public void testBuildTypeDeserializer_whenIncludeAsIsProperty_thenReturnAsPropertyTypeDeserializer() {
        stdTypeResolverBuilder.inclusion(JsonTypeInfo.As.PROPERTY).typeProperty("type");
        TypeDeserializer actual = stdTypeResolverBuilder.buildTypeDeserializer(null, baseType, subtypes);
        
        Assert.assertNotNull(actual);
        Assert.assertTrue(actual instanceof AsPropertyTypeDeserializer);
    }
    
    @Test
    public void testBuildTypeDeserializer_whenIncludeAsIsWrapperObject_thenReturnAsWrapperTypeDeserializer() {
        stdTypeResolverBuilder.inclusion(JsonTypeInfo.As.WRAPPER_OBJECT);
        TypeDeserializer actual = stdTypeResolverBuilder.buildTypeDeserializer(null, baseType, subtypes);
        
        Assert.assertNotNull(actual);
        Assert.assertTrue(actual instanceof AsWrapperTypeDeserializer);
    }
    
    @Test
    public void testBuildTypeDeserializer_whenIncludeAsIsExternalProperty_thenReturnAsExternalTypeDeserializer() {
        stdTypeResolverBuilder.inclusion(JsonTypeInfo.As.EXTERNAL_PROPERTY).typeProperty("type");
        TypeDeserializer actual = stdTypeResolverBuilder.buildTypeDeserializer(null, baseType, subtypes);
        
        Assert.assertNotNull(actual);
        Assert.assertTrue(actual instanceof AsExternalTypeDeserializer);
    }
    
    @Test(expected = IllegalStateException.class)
    public void testBuildTypeDeserializer_whenIncludeAsIsInvalid_thenThrowIllegalStateException() {
        stdTypeResolverBuilder.inclusion(null);
        stdTypeResolverBuilder.buildTypeDeserializer(null, baseType, subtypes);
    }
    
    @Test
    public void testTypeProperty_whenTypeIdPropNameIsNull_thenRestoreToDefault() {
        stdTypeResolverBuilder.typeProperty(null);
        
        String actual = stdTypeResolverBuilder.getTypeProperty();
        String expected = "type";
        
        Assert.assertEquals(expected, actual);
    }
    
    @Test
    public void testDefaultImpl() {
        Class<?> defaultImpl = String.class;
        
        StdTypeResolverBuilder actual = stdTypeResolverBuilder.defaultImpl(defaultImpl);
        
        Assert.assertEquals(stdTypeResolverBuilder, actual);
    }
    
    @Test
    public void testTypeIdVisibility() {
        boolean isVisible = true;
        
        StdTypeResolverBuilder actual = stdTypeResolverBuilder.typeIdVisibility(isVisible);
        
        Assert.assertEquals(stdTypeResolverBuilder, actual);
    }
    
    @Test
    public void testGetDefaultImpl() {
        Class<?> defaultImpl = String.class;
        stdTypeResolverBuilder.defaultImpl(defaultImpl);
        
        Class<?> actual = stdTypeResolverBuilder.getDefaultImpl();
        
        Assert.assertEquals(defaultImpl, actual);
    }
    
    @Test
    public void testIsTypeIdVisible() {
        boolean isVisible = true;
        stdTypeResolverBuilder.typeIdVisibility(isVisible);
        
        boolean actual = stdTypeResolverBuilder.isTypeIdVisible();
        
        Assert.assertEquals(isVisible, actual);
    }
}