import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.core.base.GeneratorBase;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class GeneratorBaseTest {

    @Test
    public void testConstructor() throws IOException {
        JsonFactory factory = new JsonFactory();
        GeneratorBase generator = new GeneratorBase(GeneratorBase.DEFAULT_FEATURES, factory);
        assertEquals(GeneratorBase.DEFAULT_FEATURES, generator.getFeatureMask());
        assertTrue(generator instanceof JsonGenerator);
    }

    @Test
    public void testVersion() throws IOException {
        JsonFactory factory = new JsonFactory();
        GeneratorBase generator = new GeneratorBase(GeneratorBase.DEFAULT_FEATURES, factory);
        Version version = generator.version();
        String actualVersion = version.toString();
        String expectedVersion = "unknown"; // Replace with the expected version string
        assertEquals(expectedVersion, actualVersion);
    }

    @Test
    public void testSetCurrentValue() throws IOException {
        JsonFactory factory = new JsonFactory();
        GeneratorBase generator = new GeneratorBase(GeneratorBase.DEFAULT_FEATURES, factory);
        Object currentValue = "test";
        generator.setCurrentValue(currentValue);
        assertEquals(currentValue, generator.getCurrentValue());
    }

    @Test
    public void testIsEnabled() throws IOException {
        JsonFactory factory = new JsonFactory();
        GeneratorBase generator = new GeneratorBase(GeneratorBase.DEFAULT_FEATURES | GeneratorBase.Feature.WRITE_NUMBERS_AS_STRINGS.getMask(), factory);
        assertTrue(generator.isEnabled(GeneratorBase.Feature.WRITE_NUMBERS_AS_STRINGS));
        assertFalse(generator.isEnabled(GeneratorBase.Feature.ESCAPE_NON_ASCII));
    }

    @Test
    public void testEnableFeature() throws IOException {
        JsonFactory factory = new JsonFactory();
        GeneratorBase generator = new GeneratorBase(GeneratorBase.DEFAULT_FEATURES, factory);
        generator.enable(GeneratorBase.Feature.WRITE_NUMBERS_AS_STRINGS);
        assertTrue(generator.isEnabled(GeneratorBase.Feature.WRITE_NUMBERS_AS_STRINGS));
    }

    @Test
    public void testDisableFeature() throws IOException {
        JsonFactory factory = new JsonFactory();
        GeneratorBase generator = new GeneratorBase(GeneratorBase.DEFAULT_FEATURES | GeneratorBase.Feature.WRITE_NUMBERS_AS_STRINGS.getMask(), factory);
        generator.disable(GeneratorBase.Feature.WRITE_NUMBERS_AS_STRINGS);
        assertFalse(generator.isEnabled(GeneratorBase.Feature.WRITE_NUMBERS_AS_STRINGS));
    }

    @Test
    public void testGetPrettyPrinter() throws IOException {
        JsonFactory factory = new JsonFactory();
        GeneratorBase generator = new GeneratorBase(GeneratorBase.DEFAULT_FEATURES, factory);
        assertNull(generator.getPrettyPrinter());
        generator.useDefaultPrettyPrinter();
        assertSame(GeneratorBase.DefaultPrettyPrinter.class, generator.getPrettyPrinter().getClass());
    }

    @Test
    public void testWriteString() throws IOException {
        JsonFactory factory = new JsonFactory();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        GeneratorBase generator = new GeneratorBase(GeneratorBase.DEFAULT_FEATURES, factory) {
            @Override
            public void writeString(String text) throws IOException {
                outputStream.write(text.getBytes());
            }
        };
        generator.writeString("test");
        String actualOutput = outputStream.toString();
        String expectedOutput = "\"test\"";
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testWriteRawValue() throws IOException {
        JsonFactory factory = new JsonFactory();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        GeneratorBase generator = new GeneratorBase(GeneratorBase.DEFAULT_FEATURES, factory) {
            @Override
            public void writeRawValue(String text) throws IOException {
                outputStream.write(text.getBytes());
            }
        };
        generator.writeRawValue("test");
        String actualOutput = outputStream.toString();
        String expectedOutput = "test";
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testWriteRawValueWithOffset() throws IOException {
        JsonFactory factory = new JsonFactory();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        String text = "test";
        GeneratorBase generator = new GeneratorBase(GeneratorBase.DEFAULT_FEATURES, factory) {
            @Override
            public void writeRawValue(String text, int offset, int length) throws IOException {
                outputStream.write(text.substring(offset, offset + length).getBytes());
            }
        };
        generator.writeRawValue(text, 1, 3);
        String actualOutput = outputStream.toString();
        String expectedOutput = "est";
        assertEquals(expectedOutput, actualOutput);
    }

    @Test(expected = IOException.class)
    public void testWriteRawValueWithNegativeOffset() throws IOException {
        JsonFactory factory = new JsonFactory();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        String text = "test";
        GeneratorBase generator = new GeneratorBase(GeneratorBase.DEFAULT_FEATURES, factory) {
            @Override
            public void writeRawValue(String text, int offset, int length) throws IOException {
                outputStream.write(text.substring(offset, offset + length).getBytes());
            }
        };
        generator.writeRawValue(text, -1, 3);
    }

    @Test(expected = IOException.class)
    public void testWriteRawValueWithNegativeLength() throws IOException {
        JsonFactory factory = new JsonFactory();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        String text = "test";
        GeneratorBase generator = new GeneratorBase(GeneratorBase.DEFAULT_FEATURES, factory) {
            @Override
            public void writeRawValue(String text, int offset, int length) throws IOException {
                outputStream.write(text.substring(offset, offset + length).getBytes());
            }
        };
        generator.writeRawValue(text, 0, -3);
    }

    @Test
    public void testWriteNumber() throws IOException {
        JsonFactory factory = new JsonFactory();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        GeneratorBase generator = new GeneratorBase(GeneratorBase.DEFAULT_FEATURES | GeneratorBase.Feature.WRITE_NUMBERS_AS_STRINGS.getMask(), factory) {
            @Override
            public void writeNumber(int number) throws IOException {
                outputStream.write(Integer.toString(number).getBytes());
            }
        };
        generator.writeNumber(123);
        String actualOutput = outputStream.toString();
        String expectedOutput = "123";
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testWriteNumberWithBigDecimal() throws IOException {
        JsonFactory factory = new JsonFactory();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        GeneratorBase generator = new GeneratorBase(GeneratorBase.DEFAULT_FEATURES, factory) {
            @Override
            public void writeNumber(BigDecimal bigDecimal) throws IOException {
                outputStream.write(bigDecimal.toString().getBytes());
            }
        };
        BigDecimal bigDecimal = new BigDecimal("123.456");
        generator.writeNumber(bigDecimal);
        String actualOutput = outputStream.toString();
        String expectedOutput = "123.456";
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testWriteBoolean() throws IOException {
        JsonFactory factory = new JsonFactory();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        GeneratorBase generator = new GeneratorBase(GeneratorBase.DEFAULT_FEATURES, factory) {
            @Override
            public void writeBoolean(boolean value) throws IOException {
                if (value) {
                    outputStream.write("true".getBytes());
                } else {
                    outputStream.write("false".getBytes());
                }
            }
        };
        generator.writeBoolean(true);
        String actualOutput = outputStream.toString();
        String expectedOutput = "true";
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testWriteNull() throws IOException {
        JsonFactory factory = new JsonFactory();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        GeneratorBase generator = new GeneratorBase(GeneratorBase.DEFAULT_FEATURES, factory) {
            @Override
            public void writeNull() throws IOException {
                outputStream.write("null".getBytes());
            }
        };
        generator.writeNull();
        String actualOutput = outputStream.toString();
        String expectedOutput = "null";
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testWriteObject() throws IOException {
        JsonFactory factory = new JsonFactory();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        GeneratorBase generator = new GeneratorBase(GeneratorBase.DEFAULT_FEATURES, factory) {
            @Override
            public void writeObject(Object object) throws IOException {
                if (object == null) {
                    generator.writeNull();
                } else {
                    outputStream.write("{\"class\":\"".getBytes());
                    outputStream.write(object.getClass().getName().getBytes());
                    outputStream.write("\"}".getBytes());
                }
            }
        };
        Object object = new Object() {
        };
        generator.writeObject(object);
        String actualOutput = outputStream.toString();
        String expectedOutput = "{\"class\":\"Object\"}";
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testWriteTree() throws IOException {
        JsonFactory factory = new JsonFactory();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        GeneratorBase generator = new GeneratorBase(GeneratorBase.DEFAULT_FEATURES, factory) {
            @Override
            public void writeTree(TreeNode tree) throws IOException {
                if (tree == null) {
                    generator.writeNull();
                } else {
                    outputStream.write("{\"class\":\"TreeNode\",\"value\":", tree.getClass().getCanonicalName(), ",\"children\":[");
                    for (Object child : tree) {
                        generator.writeTree(child);
                    }
                    outputStream.write("]}".getBytes());
                }
            }
        };
        TreeNode tree = new TreeNode() {
            Object value;
            TreeNode[] children = new TreeNode[3];

            static class TreeNode {
                Object value;
                TreeNode[] children;
            }
        };
        tree.children[0] = new TreeNode() {{ value = "test1"; }};
        tree.children[1] = new TreeNode() {{ value = "test2"; }};
        tree.children[2] = new TreeNode() {{ value = "test3"; }};
        generator.writeTree(tree);
        String actualOutput = outputStream.toString();
        String expectedOutput = "{\"class\":\"TreeNode\",\"value\":\"TreeNode\",\"children\":[{\"class\":\"TreeNode\",\"value\":\"Object\",\"children\":[{\"class\":\"TreeNode\",\"value\":\"Object\",\"children\":[]},{\"class\":\"TreeNode\",\"value\":\"Object\",\"children\":[]},{\"class\":\"TreeNode\",\"value\":\"Object\",\"children\":[]}]},{\"class\":\"TreeNode\",\"value\":\"Object\",\"children\":[{\"class\":\"TreeNode\",\"value\":\"Object\",\"children\":[]},{\"class\":\"TreeNode\",\"value\":\"Object\",\"children\":[]},{\"class\":\"TreeNode\",\"value\":\"Object\",\"children\":[]}]},{\"class\":\"TreeNode\",\"value\":\"Object\",\"children\":[{\"class\":\"TreeNode\",\"value\":\"Object\",\"children\":[]},{\"class\":\"TreeNode\",\"value\":\"Object\",\"children\":[]},{\"class\":\"TreeNode\",\"value\":\"Object\",\"children\":[]}]]}";
        assertEquals(expectedOutput, actualOutput);
    }
}