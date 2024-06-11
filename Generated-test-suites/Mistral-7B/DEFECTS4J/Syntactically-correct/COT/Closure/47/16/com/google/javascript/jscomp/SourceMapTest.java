import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class SourceMapTest {

  private SourceMap sourceMap;
  private SourceMapGenerator generator;
  private ByteArrayOutputStream byteArrayOutputStream;

  @Before
  public void setUp() {
    generator = SourceMapGeneratorFactory.getInstance(SourceMapFormat.DEFAULT);
    byteArrayOutputStream = new ByteArrayOutputStream();
    sourceMap = new SourceMap(generator);
  }

  @Test
  public void testGetInstance_V1() {
    SourceMap sm = SourceMap.Format.V1.getInstance().getInstance();
    assertNotNull(sm);
  }

  @Test
  public void testGetInstance_DEFAULT() {
    SourceMap sm = SourceMap.Format.DEFAULT.getInstance().getInstance();
    assertNotNull(sm);
  }

  @Test
  public void testGetInstance_V2() {
    SourceMap sm = SourceMap.Format.V2.getInstance().getInstance();
    assertNotNull(sm);
  }

  @Test
  public void testAddMapping() throws IOException {
    Node node = Node.newNode("function test() {}");
    FilePosition outputStartPosition = new FilePosition(1, 1);
    FilePosition outputEndPosition = new FilePosition(1, 10);
    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);
    String expected =
        "1:1-1:10 \"test.js\" \"test.js\" 1:1\n";
    assertTrue(byteArrayOutputStream.toString().contains(expected));
  }

  @Test(expected = IllegalStateException.class)
  public void testAddMapping_NullSourceFileName() {
    Node node = Node.newNode("function test() {}");
    FilePosition outputStartPosition = new FilePosition(1, 1);
    FilePosition outputEndPosition = new FilePosition(1, 10);
    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);
    sourceMap.addMapping(null, outputStartPosition, outputEndPosition);
  }

  @Test
  public void testAddMapping_NegativeLineno() {
    Node node = Node.newNode("function test() {}");
    FilePosition outputStartPosition = new FilePosition(-1, 1);
    FilePosition outputEndPosition = new FilePosition(-1, 10);
    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);
  }

  @Test
  public void testAppendTo() throws IOException {
    sourceMap.appendTo(byteArrayOutputStream, "test.js");
    String expected =
        "Sources:\n" +
        "test.js\n";
    assertTrue(byteArrayOutputStream.toString().contains(expected));
  }

  @Test
  public void testReset() {
    sourceMap.appendTo(byteArrayOutputStream, "test.js");
    sourceMap.reset();
    String expected = "";
    assertTrue(byteArrayOutputStream.toString().contains(expected));
  }

  @Test
  public void testSetStartingPosition() {
    int offsetLine = 1;
    int offsetIndex = 1;
    sourceMap.setStartingPosition(offsetLine, offsetIndex);
    String expected = "1:1\n";
    assertTrue(byteArrayOutputStream.toString().contains(expected));
  }

  @Test
  public void testSetWrapperPrefix() {
    String prefix = "prefix";
    sourceMap.setWrapperPrefix(prefix);
    String expected = "{\"mappings\":[],\"file\":\"test.js\",\"version\":3,\"sourcesContent\":{},\"sourceRoot\":\"\",\"name\":\"test.js\"}"
        + "\n";
    assertTrue(byteArrayOutputStream.toString().contains(expected));
  }

  @Test
  public void testValidate() throws IOException {
    sourceMap.appendTo(byteArrayOutputStream, "test.js");
    sourceMap.validate(true);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testValidate_False() throws IOException {
    sourceMap.appendTo(byteArrayOutputStream, "test.js");
    sourceMap.validate(false);
  }

  @Test
  public void testSetPrefixMappings() throws IOException {
    List<SourceMap.LocationMapping> sourceMapLocationMappings =
        Lists.newArrayList(new SourceMap.LocationMapping("foo", "bar"));
    sourceMap.setPrefixMappings(sourceMapLocationMappings);
    String expected = "test.js\n" +
        "barfoo.test.js\n";
    String actual = byteArrayOutputStream.toString();
    assertTrue(actual.contains(expected));
  }

}