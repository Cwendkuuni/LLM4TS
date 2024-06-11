

import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * Test class for PropertyBuilder
 */
public class PropertyBuilderTest {

    private PropertyBuilder propertyBuilder;

    @Before
    public void setUp() {
        propertyBuilder = new PropertyBuilder();
    }

    @Test
    public void testBuildWriterWithNullSerDefAndUnwrapper() throws Exception {
        BeanPropertyDefinition propDef = getPropertyDefinition();
        PropertyMetadata metadata = getMetadata();
        BeanPropertyWriter writer = propertyBuilder.buildWriter(null, propDef, null, null, null, null, null, false);

        assertSame(propDef, writer.getPropertyDefinition());
        assertEquals(metadata, writer.getMetadata());
        assertTrue(writer.getNonTrivialInclusions().isEmpty());
    }

    @Test
    public void testBuildWriterWithSerDefAndUnwrapper() throws Exception {
        Object serDef = new Object();
        NameTransformer unwrapper = new NameTransformerBase() {
            @Override
            public String reverse(String transformed) {
                return transformed;
            }

            @Override
            public String toString() {
                return "Unwrapper";
            }
        };
        BeanPropertyDefinition propDef = getPropertyDefinition();
        PropertyMetadata metadata = getMetadata();
        BeanPropertyWriter writer = propertyBuilder.buildWriter(null, propDef, null, null, null, null, null, false);

        assertSame(propDef, writer.getPropertyDefinition());
        assertSame(serDef, writer.getNullSerializer());
        assertSame(unwrapper, FieldUtils.readField(writer, "unwrapper", true));
        assertEquals(metadata, writer.getMetadata());
        assertTrue(writer.getNonTrivialInclusions().isEmpty());
    }

    private BeanPropertyDefinition getPropertyDefinition() {
        return new BeanPropertyDefinition() {
            @Override
            public AnnotatedMember getPrimaryMember() {
                return null;
            }

            @Override
            public Access getAccessor() {
                return null;
            }

            @Override
            public boolean isExplicitlyIncluded() {
                return false;
            }

            @Override
            public boolean isExplicitlyNamed() {
                return false;
            }

            @Override
            public PropertyMetadata getMetadata() {
                return null;
            }

            @Override
            public JsonInclude.Value findInclusion() {
                return null;
            }
        };
    }

    private PropertyMetadata getMetadata() {
        return new PropertyMetadata(JsonInclude.Value.empty(), null, null, null);
    }
}

