import static org.junit.Assert.*;
import static com.google.javascript.jscomp.SourceMap.DetailLevel.*;
import static com.google.javascript.jscomp.SourceMap.Format.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.debugging.sourcemap.FilePosition;
import com.google.debugging.sourcemap.SourceMapFormat;
import com.google.debugging.sourcemap.SourceMapGenerator;
import com.google.debugging.sourcemap.SourceMapGeneratorFactory;
import com.google.debugging.sourcemap.SourceMapGeneratorV1;
import com.google.debugging.sourcemap.SourceMapGeneratorV2;
import com.google.javascript.jscomp.Node;
import com.google.javascript.rhino.NodeUtil;

/**
 * Test class for {@link SourceMap} class.
 */
public class SourceMapTest {

  @Test
  public void testGetInstance() {
    for (SourceMap.Format format : SourceMap.Format.values()) {
      SourceMap sourceMap = format.getInstance();
      assertNotNull(sourceMap);
    }
  }

  /**
   * Test method for {@link SourceMap#addMapping(Node, FilePosition, FilePosition)}.
   *
   * @throws IOException
   */
  @Test
  public void testAddMapping() throws IOException {
    SourceMap sourceMap = V1.getInstance();

    String sourceFile = "test.js";
    Node node = Node.newNode("foo");
    FilePosition outputStartPosition = new FilePosition(1, 1);
    FilePosition outputEndPosition = new FilePosition(1, 5);

    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);
    assertEquals(sourceFile, sourceMap.fixupSourceLocation(sourceFile));

    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    sourceMap.appendTo(outputStream, "test.js.map");
    String mapContent = outputStream.toString();
    assertTrue(mapContent.contains(sourceFile + ":" + outputStartPosition.getLine()
        + ":" + outputStartPosition.getColumn()));
  }

  /**
   * Test method for {@link SourceMap#appendTo(Appendable, String)}.
   *
   * @throws IOException
   */
  @Test
  public void testAppendTo() throws IOException {
    SourceMap sourceMap = V1.getInstance();

    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    sourceMap.appendTo(outputStream, "test.js.map");
    String mapContent = outputStream.toString();
    assertEmpty(mapContent);

    sourceMap.addMapping(Node.newNode("foo"), null, null);
    sourceMap.appendTo(outputStream, "test.js.map");
    String mapContentAfterAddMapping = outputStream.toString();
    assertTrue(mapContentAfterAddMapping.contains("test.js:1:0"));
  }

  /**
   * Test method for {@link SourceMap#reset()}.
   */
  @Test
  public void testReset() {
    SourceMap sourceMap = V1.getInstance();

    sourceMap.addMapping(Node.newNode("foo"), null, null);

    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    sourceMap.appendTo(outputStream, "test.js.map");
    String mapContentAfterAddMapping = outputStream.toString();
    assertTrue(mapContentAfterAddMapping.contains("test.js:1:0"));

    sourceMap.reset();

    ByteArrayOutputStream outputStreamAfterReset = new ByteArrayOutputStream();
    sourceMap.appendTo(outputStreamAfterReset, "test.js.map");
    String mapContentAfterReset = outputStreamAfterReset.toString();
    assertEmpty(mapContentAfterReset);
  }

  /**
   * Test method for
   * {@link SourceMap#setStartingPosition(int, int)}.
   *
   * @throws IOException
   */
  @Test
  public void testSetStartingPosition() throws IOException {
    SourceMap sourceMap = V1.getInstance();

    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    sourceMap.appendTo(outputStream, "test.js.map");
    String mapContentBeforeSettingStartingPosition = outputStream.toString();
    assertEmpty(mapContentBeforeSettingStartingPosition);

    sourceMap.setStartingPosition(1, 0);
    sourceMap.addMapping(Node.newNode("foo"), null, null);
    sourceMap.appendTo(outputStream, "test.js.map");
    String mapContentAfterSettingStartingPosition = outputStream.toString();
    assertTrue(mapContentAfterSettingStartingPosition.contains("test.js:1:0"));

    assertEquals(1, sourceMap.getStartingPosition().getLine());
    assertEquals(0, sourceMap.getStartingPosition().getColumn());

    ByteArrayOutputStream outputStreamAfterReset = new ByteArrayOutputStream();
    sourceMap.appendTo(outputStreamAfterReset, "test.js.map");
    String mapContentAfterReset = outputStreamAfterReset.toString();
    assertEmpty(mapContentAfterReset);

    sourceMap.reset();

    ByteArrayOutputStream outputStreamAfterSecondReset = new ByteArrayOutputStream();
    sourceMap.appendTo(outputStreamAfterSecondReset, "test.js.map");
    String mapContentAfterSecondReset = outputStreamAfterSecondReset.toString();
    assertEmpty(mapContentAfterSecondReset);
  }

  /**
   * Test method for
   * {@link SourceMap#setWrapperPrefix(String)}.
   *
   * @throws IOException
   */
  @Test
  public void testSetWrapperPrefix() throws IOException {
    SourceMap sourceMap = V1.getInstance();

    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    sourceMap.appendTo(outputStream, "test.js.map");
    String mapContentBeforeSettingWrapperPrefix = outputStream.toString();
    assertEmpty(mapContentBeforeSettingWrapperPrefix);

    sourceMap.setWrapperPrefix("prefix");
    sourceMap.addMapping(Node.newNode("foo"), null, null);
    sourceMap.appendTo(outputStream, "test.js.map");
    String mapContentAfterSettingWrapperPrefix = outputStream.toString();
    assertTrue(mapContentAfterSettingWrapperPrefix.contains("prefixtest.js:1:0"));

    assertEquals("prefix", sourceMap.getWrapperPrefix());

    ByteArrayOutputStream outputStreamAfterReset = new ByteArrayOutputStream();
    sourceMap.appendTo(outputStreamAfterReset, "test.js.map");
    String mapContentAfterReset = outputStreamAfterReset.toString();
    assertEmpty(mapContentAfterReset);

    sourceMap.reset();

    ByteArrayOutputStream outputStreamAfterSecondReset = new ByteArrayOutputStream();
    sourceMap.appendTo(outputStreamAfterSecondReset, "test.js.map");
    String mapContentAfterSecondReset = outputStreamAfterSecondReset.toString();
    assertEmpty(mapContentAfterSecondReset);
  }

  /**
   * Test method for
   * {@link SourceMap#validate(boolean)}.
   *
   * @throws IOException
   */
  @Test(expected = IOException.class)
  public void testValidate() throws IOException {
    SourceMap sourceMap = V1.getInstance();

    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    sourceMap.appendTo(outputStream, "test.js.map");
    String mapContentBeforeValidation = outputStream.toString();
    assertEmpty(mapContentBeforeValidation);

    sourceMap.validate(true);

    ByteArrayOutputStream outputStreamAfterValidation = new ByteArrayOutputStream();
    sourceMap.appendTo(outputStreamAfterValidation, "test.js.map");
    String mapContentAfterValidation = outputStreamAfterValidation.toString();
    assertTrue(mapContentAfterValidation.contains("test.js:1:0"));

    sourceMap.validate(false);
  }

  /**
   * Test method for {@link SourceMap#setPrefixMappings(List)}.
   *
   * @throws IOException
   */
  @Test
  public void testSetPrefixMappings() throws IOException {
    String sourceFile1 = "test1.js";
    String sourceFile2 = "test2.js";
    String replacementFile1 = "replacement1.js";
    String replacementFile2 = "replacement2.js";

    List<LocationMapping> locationMappings = Lists.newArrayList(
        new LocationMapping(sourceFile1, replacementFile1),
        new LocationMapping(sourceFile2, replacementFile2));

    SourceMap sourceMap = V1.getInstance();
    sourceMap.setPrefixMappings(locationMappings);

    String fixedSourceFile1 = sourceMap.fixupSourceLocation(sourceFile1);
    assertEquals(replacementFile1, fixedSourceFile1);

    String fixedSourceFile2 = sourceMap.fixupSourceLocation(sourceFile2);
    assertEquals(replacementFile2, fixedSourceFile2);

    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    sourceMap.appendTo(outputStream, "test.js.map");
    String mapContent = outputStream.toString();
    assertTrue(mapContent.contains(replacementFile1 + ":1:0"));
    assertTrue(mapContent.contains(replacementFile2 + ":1:0"));

    sourceMap.reset();

    ByteArrayOutputStream outputStreamAfterSecondReset = new ByteArrayOutputStream();
    sourceMap.appendTo(outputStreamAfterSecondReset, "test.js.map");
    String mapContentAfterSecondReset = outputStreamAfterSecondReset.toString();
    assertEmpty(mapContentAfterSecondReset);
  }
}