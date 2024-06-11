import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.SourceMap;
import com.google.javascript.jscomp.SourceMapGenerator;
import com.google.javascript.jscomp.SourceMapGeneratorFactory;
import com.google.javascript.jscomp.NodeUtil;
import static org.junit.Assert.assertEquals;

public class SourceMapTest {

  private SourceMapGenerator generator;
  private SourceMap sourceMap;
  private Node node;

  @Before
  public void setup() {
    SourceMapFormat format = Mockito.mock(SourceMapFormat.class);
    SourceMapGeneratorFactory factory = Mockito.mock(SourceMapGeneratorFactory.class);
    Mockito.when(factory.getInstance(SourceMapFormat.DEFAULT)).thenReturn(format);
    generator = Mockito.spy(new SourceMapGeneratorV1(format));
    sourceMap = new SourceMap(generator);
    node = Mockito.spy(new Node() {
      @Override
      public String getSourceFileName() {
        return "TestSourceFile.java";
      }

      @Override
      public int getLineno() {
        return 1;
      }

      @Override
      public int getCharno() {
        return 5;
      }
    });
  }

  @Test
  public void testAddMapping() {
    FilePosition outputStartPosition = new FilePosition(2, 7);
    FilePosition outputEndPosition = new FilePosition(3, 12);
    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);

    String sourceFile = sourceMap.fixupSourceLocation("TestSourceFile.java");
    assertEquals("TestSourceFile.java", sourceFile);

    try {
      sourceMap.appendTo(Mockito.mock(Appendable.class), "sourcemap.js");
    } catch (IOException e) {
      e.printStackTrace();
    }

    sourceMap.reset();
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddMappingWithNullNode() {
    FilePosition outputStartPosition = new FilePosition(2, 7);
    FilePosition outputEndPosition = new FilePosition(3, 12);
    sourceMap.addMapping(null, outputStartPosition, outputEndPosition);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddMappingWithNullStartPosition() {
    Node node = Mockito.spy(new Node() {});
    sourceMap.addMapping(node, null, new FilePosition(2, 7));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddMappingWithNullEndPosition() {
    Node node = Mockito.spy(new Node() {});
    sourceMap.addMapping(node, new FilePosition(2, 7), null);
  }

  @Test
  public void testFixupSourceLocation() {
    String sourceFile = "original/TestSourceFile.java";
    List<LocationMapping> sourceMapLocationMappings = Collections.singletonList(
        new LocationMapping("original", "fixed"));
    sourceMap.setPrefixMappings(sourceMapLocationMappings);

    String fixedSourceFile = sourceMap.fixupSourceLocation(sourceFile);
    assertEquals("fixed/TestSourceFile.java", fixedSourceFile);
  }

  @Test
  public void testFixupSourceLocationWithEmptyPrefixMappings() {
    String sourceFile = "original/TestSourceFile.java";
    sourceMap.setPrefixMappings(Collections.emptyList());

    String fixedSourceFile = sourceMap.fixupSourceLocation(sourceFile);
    assertEquals("original/TestSourceFile.java", fixedSourceFile);
  }

  @Test
  public void testSetPrefixMappings() {
    List<LocationMapping> sourceMapLocationMappings = Collections.singletonList(
        new LocationMapping("original", "fixed"));
    sourceMap.setPrefixMappings(sourceMapLocationMappings);

    String fixedSourceFile = sourceMap.fixupSourceLocation("original/TestSourceFile.java");
    assertEquals("fixed/TestSourceFile.java", fixedSourceFile);
  }
}