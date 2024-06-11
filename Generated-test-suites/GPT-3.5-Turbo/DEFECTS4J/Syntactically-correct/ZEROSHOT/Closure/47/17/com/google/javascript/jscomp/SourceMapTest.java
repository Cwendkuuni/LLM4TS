import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class SourceMapTest {

  private SourceMap sourceMap;

  @Before
  public void setUp() {
    sourceMap = SourceMap.Format.DEFAULT.getInstance();
    sourceMap.setWrapperPrefix("prefix");
  }

  @Test
  public void testAddMapping() {
    Node node = new Node("test", 1, 1);
    FilePosition startPos = new FilePosition(1, 1);
    FilePosition endPos = new FilePosition(2, 2);

    sourceMap.addMapping(node, startPos, endPos);

    // Verify that the mapping is added correctly
    // ... assert statements ...
  }

  @Test
  public void testFixupSourceLocation() {
    String sourceFile = "path/to/source.file";
    String fixed = sourceMap.fixupSourceLocation(sourceFile);

    // Verify that the source file is fixed up correctly
    // ... assert statements ...
  }

  @Test
  public void testSetWrapperPrefix() {
    String prefix = "newPrefix";
    sourceMap.setWrapperPrefix(prefix);

    // Verify that the wrapper prefix is set correctly
    // ... assert statements ...
  }

  @Test
  public void testSetPrefixMappings() {
    List<LocationMapping> mappings = new ArrayList<>();
    mappings.add(new LocationMapping("prefix1", "replacement1"));
    mappings.add(new LocationMapping("prefix2", "replacement2"));
    sourceMap.setPrefixMappings(mappings);

    // Verify that the prefix mappings are set correctly
    // ... assert statements ...
  }

  @Test
  public void testValidate() {
    boolean validate = true;
    sourceMap.validate(validate);

    // Verify that the validation value is set correctly
    // ... assert statements ...
  }

  @Test
  public void testReset() {
    sourceMap.reset();

    // Verify that the source map is reset correctly
    // ... assert statements ...
  }

  @Test
  public void testSetStartingPosition() {
    int offsetLine = 5;
    int offsetIndex = 10;
    sourceMap.setStartingPosition(offsetLine, offsetIndex);

    // Verify that the starting position is set correctly
    // ... assert statements ...
  }

  @Test
  public void testAppendTo() throws IOException {
    StringBuilder sb = new StringBuilder();
    String name = "sourceMap";

    sourceMap.appendTo(sb, name);

    // Verify that the source map is appended correctly
    // ... assert statements ...
  }

}