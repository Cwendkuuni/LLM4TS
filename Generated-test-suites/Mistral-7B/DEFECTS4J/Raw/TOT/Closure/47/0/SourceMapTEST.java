import com.google.javascript.jscomp.SourceMap;
import com.google.javascript.jscomp.SourceMap.Format;
import com.google.javascript.jscomp.SourceMap.DetailLevel;
import org.junit.Test;
import static org.junit.Assert.assertNotNull;

public class SourceMapTest {

  @Test
  public void testSourceMapV1() throws IOException {
    SourceMap sourceMap = SourceMap.Format.V1.getInstance().newInstance();
    Node node = new Node(); // Create a dummy node for testing
    FilePosition outputStartPosition = new FilePosition(1, 1); // Output file position
    FilePosition outputEndPosition = new FilePosition(1, 10); // Output file position

    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);
    sourceMap.appendTo(System.out, "sourcemap.v1.js");
    sourceMap.reset();

    assertNotNull(sourceMap.getInstance()); // Verify instance is not null
  }

  @Test
  public void testSourceMapDefault() throws IOException {
    SourceMap sourceMap = SourceMap.Format.DEFAULT.getInstance().newInstance();
    Node node = new Node(); // Create a dummy node for testing
    FilePosition outputStartPosition = new FilePosition(1, 1); // Output file position
    FilePosition outputEndPosition = new FilePosition(1, 10); // Output file position

    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);
    sourceMap.appendTo(System.out, "sourcemap.default.js");
    sourceMap.reset();

    assertNotNull(sourceMap.getInstance()); // Verify instance is not null
  }

  @Test
  public void testSourceMapV2() throws IOException {
    SourceMap sourceMap = SourceMap.Format.V2.getInstance().newInstance();
    Node node = new Node(); // Create a dummy node for testing
    FilePosition outputStartPosition = new FilePosition(1, 1); // Output file position
    FilePosition outputEndPosition = new FilePosition(1, 10); // Output file position

    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);
    sourceMap.appendTo(System.out, "sourcemap.v2.js");
    sourceMap.reset();

    assertNotNull(sourceMap.getInstance()); // Verify instance is not null
  }

  @Test
  public void testSourceMapDetailLevel() throws IOException {
    SourceMap sourceMap = SourceMap.newInstance();
    Node node = new Node(); // Create a dummy node for testing
    FilePosition outputStartPosition = new FilePosition(1, 1); // Output file position
    FilePosition outputEndPosition = new FilePosition(1, 10); // Output file position

    sourceMap.setDetailLevel(DetailLevel.SYMBOLS);
    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);
    sourceMap.appendTo(System.out, "sourcemap.symbols.js");
    sourceMap.reset();

    assertNotNull(sourceMap.getInstance()); // Verify instance is not null
  }
}