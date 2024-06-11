import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CodingConvention.*;
import static com.google.javascript.rhino.JSType.*;
import static com.google.javascript.rhino.JSType.getNativeType;

import org.junit.Test;

public class ClosureReverseAbstractInterpreterTest {

  @Test
  public void testConstructor() {
    CodingConvention convention = new CodingConvention();
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    ClosureReverseAbstractInterpreter ra = new ClosureReverseAbstractInterpreter(convention, typeRegistry);
    assertNotNull(ra);
  }
}