import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.debugging.sourcemap.FilePosition;
import com.google.debugging.sourcemap.SourceMapFormat;
import com.google.debugging.sourcemap.SourceMapGenerator;
import com.google.debugging.sourcemap.SourceMapGeneratorFactory;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.SourceMap;
import com.google.javascript.jscomp.SourceMap.Format;
import com.google.javascript.jscomp.SourceMap.DetailLevel;
import com.google.javascript.rhino.NodeUtil;

public class SourceMapTest {

  private SourceMapGenerator generator;
  private SourceMap sourceMap;

  @Before
  public void setup() throws IOException {
    generator = mock(SourceMapGenerator.class);
    SourceMapGeneratorFactory factory = mock(SourceMapGeneratorFactory.class);
    when(factory.getInstance(SourceMapFormat.DEFAULT)).thenReturn(generator);
    sourceMap = new SourceMap(factory.getInstance(SourceMapFormat.DEFAULT));
  }

  @Test
  public void testAddMapping() {
    Node node = mock(Node.class);
    FilePosition outputStartPosition = new FilePosition(1, 1);
    FilePosition outputEndPosition = new FilePosition(1, 2);
    String sourceFile = "test.js";

    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);

    assertNotNull(sourceMap.generator);
    verifyMappingAdded(sourceFile, node.getLineno(), node.getCharno(), outputStartPosition, outputEndPosition);
  }

  @Test
  public void testAddMappingWithPrefixMappings() {
    Node node = mock(Node.class);
    FilePosition outputStartPosition = new FilePosition(1, 1);
    FilePosition outputEndPosition = new FilePosition(1, 2);
    String sourceFile = "test/prefix/mappings/test.js";

    List<SourceMap.LocationMapping> sourceMapLocationMappings = Lists.newArrayList(
        new SourceMap.LocationMapping("test/prefix/mappings", "test.js")
    );

    sourceMap.setPrefixMappings(sourceMapLocationMappings);

    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);

    String fixedSourceFile = sourceMap.fixupSourceLocation(sourceFile);

    assertNotNull(fixedSourceFile);
    assertEquals("test.js", fixedSourceFile);

    verifyMappingAdded(sourceFile, node.getLineno(), node.getCharno(), outputStartPosition, outputEndPosition);
  }

  @Test
  public void testAppendTo() throws IOException {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    String name = "test.js.map";

    sourceMap.appendTo(out, name);

    String actual = out.toString();
    String expected = generator.getContentsAsString();

    assertEquals(expected, actual);
  }

  @Test
  public void testReset() {
    sourceMap.reset();

    verify(generator).reset();
  }

  @Test
  public void testSetStartingPosition() {
    int offsetLine = 1;
    int offsetIndex = 1;

    sourceMap.setStartingPosition(offsetLine, offsetIndex);

    verify(generator).setStartingPosition(offsetLine, offsetIndex);
  }

  @Test
  public void testSetWrapperPrefix() {
    String prefix = "wrapper";

    sourceMap.setWrapperPrefix(prefix);

    verify(generator).setWrapperPrefix(prefix);
  }

  @Test
  public void testValidate() {
    boolean validate = true;

    sourceMap.validate(validate);

    verify(generator).validate(validate);
  }

  private void verifyMappingAdded(String sourceFile, int lineNumber, int charPosition, FilePosition outputStartPosition, FilePosition outputEndPosition) {
    String fixedSourceFile = sourceMap.fixupSourceLocation(sourceFile);

    sourceMap.generator.assertMappingAdded(fixedSourceFile, sourceFile, lineNumber, charPosition, outputStartPosition, outputEndPosition);
  }

  private void verify(SourceMapGenerator generator) {
    // Stub out the generator's methods since we cannot mock static methods
    when(generator.getContentsAsString()).thenReturn("expected source map contents");
  }

  private void verifyMappingAdded(SourceMapGenerator generator,
      String sourceFile, int lineNumber, int charPosition, FilePosition outputStartPosition, FilePosition outputEndPosition) {
    generator.assertMappingAdded(sourceFile, sourceFile, lineNumber, charPosition, outputStartPosition, outputEndPosition);
  }

  private void verify(SourceMapGenerator generator,
      String fixedSourceFile, String sourceFile, int lineNumber, int charPosition, FilePosition outputStartPosition, FilePosition outputEndPosition) {
    generator.assertMappingAdded(fixedSourceFile, sourceFile, lineNumber, charPosition, outputStartPosition, outputEndPosition);
  }
}