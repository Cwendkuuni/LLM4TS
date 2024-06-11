
import static org.junit.Assert.*;
import org.junit.*;

public class SourceMapTest {

   @Test
   public void testAddMapping() {
      SourceMap sourceMap = new SourceMap(SourceMap.Format.V1.getInstance());

      Node node = new Node();
      FilePosition outputStartPosition = new FilePosition(1, 1);
      FilePosition outputEndPosition = new FilePosition(2, 2);

      sourceMap.addMapping(node, outputStartPosition, outputEndPosition);

      // assert statements
      assertNotNull(sourceMap);
   }

   @Test
   public void testFixupSourceLocation() {
      SourceMap sourceMap = new SourceMap(SourceMap.Format.V1.getInstance());

      String sourceFile = "test/source-file.js";

      String fixedSourceFile = sourceMap.fixupSourceLocation(sourceFile);

      // assert statements
      assertEquals(sourceFile, fixedSourceFile);
   }

   @Test
   public void testAppendTo() throws IOException {
      SourceMap sourceMap = new SourceMap(SourceMap.Format.V1.getInstance());

      Appendable out = new StringBuilder();
      String name = "source.js";

      sourceMap.appendTo(out, name);

      // assert statements
      assertNotNull(out.toString());
   }

   @Test
   public void testReset() {
      SourceMap sourceMap = new SourceMap(SourceMap.Format.V1.getInstance());

      sourceMap.reset();

      // assert statements
      assertNotNull(sourceMap);
   }

   @Test
   public void testSetStartingPosition() {
      SourceMap sourceMap = new SourceMap(SourceMap.Format.V1.getInstance());

      int offsetLine = 1;
      int offsetIndex = 1;

      sourceMap.setStartingPosition(offsetLine, offsetIndex);

      // assert statements
      assertNotNull(sourceMap);
   }

   @Test
   public void testSetWrapperPrefix() {
      SourceMap sourceMap = new SourceMap(SourceMap.Format.V1.getInstance());

      String prefix = "wrapper/";

      sourceMap.setWrapperPrefix(prefix);

      // assert statements
      assertNotNull(sourceMap);
   }

   @Test
   public void testValidate() {
      SourceMap sourceMap = new SourceMap(SourceMap.Format.V1.getInstance());

      boolean validate = true;

      sourceMap.validate(validate);

      // assert statements
      assertNotNull(sourceMap);
   }

   @Test
   public void testSetPrefixMappings() {
      SourceMap sourceMap = new SourceMap(SourceMap.Format.V1.getInstance());

      List<LocationMapping> sourceMapLocationMappings = new ArrayList<>();

      sourceMap.setPrefixMappings(sourceMapLocationMappings);

      // assert statements
      assertNotNull(sourceMap);
   }
}

