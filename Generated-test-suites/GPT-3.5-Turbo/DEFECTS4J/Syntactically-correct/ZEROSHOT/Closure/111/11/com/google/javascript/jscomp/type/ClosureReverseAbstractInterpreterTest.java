import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.javascript.jscomp.CodingConvention;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.jscomp.type.ClosureReverseAbstractInterpreter;

public class ClosureReverseAbstractInterpreterTest {

    private ClosureReverseAbstractInterpreter rai;

    @Before
    public void setUp() {
        CodingConvention convention = new CodingConvention();
        JSTypeRegistry typeRegistry = new JSTypeRegistry();
        rai = new ClosureReverseAbstractInterpreter(convention, typeRegistry);
    }

    @Test
    public void testRestrictToArrayVisitor_caseTopType() {
        // Arrange
        JSType topType = new JSType();

        // Act
        JSType result = rai.restrictToArrayVisitor.caseTopType(topType);

        // Assert
        assertEquals(topType, result);
    }

    @Test
    public void testRestrictToArrayVisitor_caseObjectType() {
        // Arrange
        ObjectType type = new ObjectType();

        // Act
        JSType result = rai.restrictToArrayVisitor.caseObjectType(type);

        // Assert
        assertNotNull(result);
    }

    @Test
    public void testRestrictToNotArrayVisitor_caseObjectType() {
        // Arrange
        ObjectType type = new ObjectType();

        // Act
        JSType result = rai.restrictToNotArrayVisitor.caseObjectType(type);

        // Assert
        assertNull(result);
    }

    @Test
    public void testRestrictToObjectVisitor_caseTopType() {
        // Arrange
        JSType topType = new JSType();

        // Act
        JSType result = rai.restrictToObjectVisitor.caseTopType(topType);

        // Assert
        assertNotNull(result);
    }

    @Test
    public void testRestrictToObjectVisitor_caseObjectType() {
        // Arrange
        ObjectType type = new ObjectType();

        // Act
        JSType result = rai.restrictToObjectVisitor.caseObjectType(type);

        // Assert
        assertNotNull(result);
    }

    @Test
    public void testRestrictToObjectVisitor_caseFunctionType() {
        // Arrange
        FunctionType type = new FunctionType();

        // Act
        JSType result = rai.restrictToObjectVisitor.caseFunctionType(type);

        // Assert
        assertNotNull(result);
    }

    @Test
    public void testRestrictToNotObjectVisitor_caseAllType() {
        // Arrange

        // Act
        JSType result = rai.restrictToNotObjectVisitor.caseAllType();

        // Assert
        assertNotNull(result);
    }

    @Test
    public void testRestrictToNotObjectVisitor_caseObjectType() {
        // Arrange
        ObjectType type = new ObjectType();

        // Act
        JSType result = rai.restrictToNotObjectVisitor.caseObjectType(type);

        // Assert
        assertNull(result);
    }

    @Test
    public void testRestrictToNotObjectVisitor_caseFunctionType() {
        // Arrange
        FunctionType type = new FunctionType();

        // Act
        JSType result = rai.restrictToNotObjectVisitor.caseFunctionType(type);

        // Assert
        assertNull(result);
    }
}