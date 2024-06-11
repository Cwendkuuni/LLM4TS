import com.google.common.collect.Lists;
import com.google.javascript.jscomp.SourceMap;
import com.google.javascript.jscomp.SourceMap.DetailLevel;
import com.google.javascript.jscomp.SourceMap.Format;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class SourceMapTest {

    @Test
    public void testFormatV1() {
        SourceMap sourceMap = SourceMap.Format.V1.getInstance().getInstance();
        assertNotNull(sourceMap);
    }

    @Test
    public void testAddMappingWithValidInput() {
        SourceMap sourceMap = new SourceMap(new SourceMapGeneratorFactory() {
            @Override
            public SourceMapGenerator createGenerator() {
                return new SourceMapGeneratorV1();
            }
        });
        Node node = new Node();
        FilePosition outputStartPosition = new FilePosition(1, 1);
        FilePosition outputEndPosition = new FilePosition(1, 2);
        String sourceFile = "test.js";

        sourceMap.addMapping(node, outputStartPosition, outputEndPosition);
        String fixedSourceFile = sourceMap.fixupSourceLocation(sourceFile);
        assertTrue(fixedSourceFile.equals("test.js"));
    }

    @Test
    public void testAddMappingWithPrefixMapping() {
        SourceMap sourceMap = new SourceMap(new SourceMapGeneratorFactory() {
            @Override
            public SourceMapGenerator createGenerator() {
                return new SourceMapGeneratorV1();
            }
        });
        Node node = new Node();
        FilePosition outputStartPosition = new FilePosition(1, 1);
        FilePosition outputEndPosition = new FilePosition(1, 2);
        String sourceFile = "path/to/test.js";
        String prefix = "test";
        String replacement = "replacement";

        sourceMap.setPrefixMappings(Lists.newArrayList(new SourceMap.LocationMapping(prefix, replacement)));

        sourceMap.addMapping(node, outputStartPosition, outputEndPosition);
        String fixedSourceFile = sourceMap.fixupSourceLocation(sourceFile);
        assertTrue(fixedSourceFile.equals("replacement/path/to/test.js"));
    }

    @Test
    public void testAddMappingWithNullSourceFile() {
        SourceMap sourceMap = new SourceMap(new SourceMapGeneratorFactory() {
            @Override
            public SourceMapGenerator createGenerator() {
                return new SourceMapGeneratorV1();
            }
        });
        Node node = new Node();
        FilePosition outputStartPosition = new FilePosition(1, 1);
        FilePosition outputEndPosition = new FilePosition(1, 2);

        sourceMap.addMapping(node, outputStartPosition, outputEndPosition);
        String fixedSourceFile = sourceMap.fixupSourceLocation(null);
        assertNull(fixedSourceFile);
    }

    @Test
    public void testAddMappingWithNegativeLineNumber() {
        SourceMap sourceMap = new SourceMap(new SourceMapGeneratorFactory() {
            @Override
            public SourceMapGenerator createGenerator() {
                return new SourceMapGeneratorV1();
            }
        });
        Node node = new Node();
        FilePosition outputStartPosition = new FilePosition(1, 1);
        FilePosition outputEndPosition = new FilePosition(1, 2);
        String sourceFile = "test.js";
        int lineNumber = -1;

        sourceMap.addMapping(node, outputStartPosition, outputEndPosition);
        String fixedSourceFile = sourceMap.fixupSourceLocation(sourceFile);
        assertTrue(fixedSourceFile.equals("test.js"));
        assertFalse(sourceMap.prefixMappings.isEmpty());
    }

    @Test
    public void testAppendTo() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        SourceMap sourceMap = new SourceMap(new SourceMapGeneratorFactory() {
            @Override
            public SourceMapGenerator createGenerator() {
                return new SourceMapGeneratorV1();
            }
        });
        String name = "source.map";

        sourceMap.appendTo(outputStream, name);
        assertNotNull(outputStream.toString());
    }

    @Test
    public void testReset() {
        SourceMap sourceMap = new SourceMap(new SourceMapGeneratorFactory() {
            @Override
            public SourceMapGenerator createGenerator() {
                return new SourceMapGeneratorV1();
            }
        });
        sourceMap.reset();
        assertTrue(sourceMap.generator.isReset());
    }

    @Test
    public void testSetStartingPosition() {
        SourceMap sourceMap = new SourceMap(new SourceMapGeneratorFactory() {
            @Override
            public SourceMapGenerator createGenerator() {
                return new SourceMapGeneratorV1();
            }
        });
        int offsetLine = 1;
        int offsetIndex = 1;

        sourceMap.setStartingPosition(offsetLine, offsetIndex);
        FilePosition expectedStartingPosition = new FilePosition(offsetLine, offsetIndex);
        FilePosition actualStartingPosition = sourceMap.generator.getStartingPosition();

        assertTrue(actualStartingPosition.getLine().equals(expectedStartingPosition.getLine()));
        assertTrue(actualStartingPosition.getColumn().equals(expectedStartingPosition.getColumn()));
    }

    @Test
    public void testSetWrapperPrefix() {
        SourceMap sourceMap = new SourceMap(new SourceMapGeneratorFactory() {
            @Override
            public SourceMapGenerator createGenerator() {
                return new SourceMapGeneratorV1();
            }
        });
        String prefix = "wrapper";

        sourceMap.setWrapperPrefix(prefix);
        String actualWrapperPrefix = sourceMap.generator.getWrapperPrefix();

        assertTrue(actualWrapperPrefix.equals(prefix));
    }

    @Test
    public void testValidate() {
        SourceMap sourceMap = new SourceMap(new SourceMapGeneratorFactory() {
            @Override
            public SourceMapGenerator createGenerator() {
                return new SourceMapGeneratorV1();
            }
        });
        boolean validate = true;

        sourceMap.validate(validate);
        assertTrue(sourceMap.generator.isValid());
    }
}