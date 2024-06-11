import org.junit.Test;
import static org.mockito.Mockito.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.jsontype.*;
import static org.junit.Assert.*;

public class StdTypeResolverBuilderTest {

    @Test
    public void testBuildTypeSerializer() throws Exception {
        JsonTypeInfo.Id idType = JsonTypeInfo.Id.CLASS;
        TypeIdResolver idRes = mock(TypeIdResolver.class);
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(idType, idRes);

        SerializationConfig config = new SerializationConfig();
        JavaType baseType = mock(JavaType.class);
        Collection<NamedType> subtypes = Collections.emptyList();

        TypeSerializer serializer = builder.buildTypeSerializer(config, baseType, subtypes);
        assertNotNull(serializer);

        verify(idRes, times(1)).getId(eq(config), eq(baseType), eq(subtypes), anyBoolean(), anyBoolean());
    }
}