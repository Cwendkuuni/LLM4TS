import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.cli2.*;

public class PatternBuilderTest {

  @Test
  public void testCreate() {
    PatternBuilder pb = new PatternBuilder();
    Option option1 = pb.create();
    Option option2 = pb.create();
    assertEquals(Option.class, option1.getClass());
    assertEquals(Option.class, option2.getClass());
  }
}