import static org.junit.Assert.assertEquals;
import static com.google.javascript.jscomp.SourceMap.Format.DEFAULT;

import org.junit.Test;

public class SourceMapTest {

  @Test
  public void testGetInstance() {
    SourceMap sourceMap = SourceMap.getInstance(DEFAULT);
    assertEquals(SourceMap.Format.DEFAULT, sourceMap.getFormat());
  }
}