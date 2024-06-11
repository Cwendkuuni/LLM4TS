import static org.junit.Assert.*;
import org.junit.Test;

public class ClosureReverseAbstractInterpreterTest {

  @Test
  public void testRestrictToArrayVisitor() {
    ClosureReverseAbstractInterpreter closureRAI = new ClosureReverseAbstractInterpreter();
    closureRAI.restrictToArrayVisitor.apply(null);
  }

  @Test
  public void testRestrictToNotArrayVisitor() {
    ClosureReverseAbstractInterpreter closureRAI = new ClosureReverseAbstractInterpreter();
    closureRAI.restrictToNotArrayVisitor.apply(null);
  }

  @Test
  public void testRestrictToObjectVisitor() {
    ClosureReverseAbstractInterpreter closureRAI = new ClosureReverseAbstractInterpreter();
    closureRAI.restrictToObjectVisitor.apply(null);
  }

  @Test
  public void testRestrictToNotObjectVisitor() {
    ClosureReverseAbstractInterpreter closureRAI = new ClosureReverseAbstractInterpreter();
    closureRAI.restrictToNotObjectVisitor.apply(null);
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome() {
    ClosureReverseAbstractInterpreter closureRAI = new ClosureReverseAbstractInterpreter();
    closureRAI.getPreciserScopeKnowingConditionOutcome(null, null, false);
  }

  @Test
  public void testRestrictParameter() {
    ClosureReverseAbstractInterpreter closureRAI = new ClosureReverseAbstractInterpreter();
    closureRAI.restrictParameter(null, null, null, null, false);
  }

  @Test
  public void testTypeRestriction() {
    ClosureReverseAbstractInterpreter.TypeRestriction typeRestriction = new ClosureReverseAbstractInterpreter.TypeRestriction(null, false);
    assertNotNull(typeRestriction);
  }

}