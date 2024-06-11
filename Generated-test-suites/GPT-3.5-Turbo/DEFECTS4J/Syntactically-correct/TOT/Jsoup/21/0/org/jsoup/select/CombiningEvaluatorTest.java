import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Element;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

class CombiningEvaluatorTest {

    @Test
    void testCombiningEvaluator_And_Matches_ReturnsTrue() {
        // Arrange
        Element rootElement = mock(Element.class);
        Element nodeElement = mock(Element.class);
        Evaluator evaluator1 = mock(Evaluator.class);
        Evaluator evaluator2 = mock(Evaluator.class);
        when(evaluator1.matches(rootElement, nodeElement)).thenReturn(true);
        when(evaluator2.matches(rootElement, nodeElement)).thenReturn(true);
        CombiningEvaluator.And andEvaluator = new CombiningEvaluator.And(evaluator1, evaluator2);

        // Act
        boolean result = andEvaluator.matches(rootElement, nodeElement);

        // Assert
        assertTrue(result);
    }

    @Test
    void testCombiningEvaluator_And_Matches_ReturnsFalse() {
        // Arrange
        Element rootElement = mock(Element.class);
        Element nodeElement = mock(Element.class);
        Evaluator evaluator1 = mock(Evaluator.class);
        Evaluator evaluator2 = mock(Evaluator.class);
        when(evaluator1.matches(rootElement, nodeElement)).thenReturn(true);
        when(evaluator2.matches(rootElement, nodeElement)).thenReturn(false);
        CombiningEvaluator.And andEvaluator = new CombiningEvaluator.And(evaluator1, evaluator2);

        // Act
        boolean result = andEvaluator.matches(rootElement, nodeElement);

        // Assert
        assertFalse(result);
    }

    @Test
    void testCombiningEvaluator_Or_Matches_ReturnsTrue() {
        // Arrange
        Element rootElement = mock(Element.class);
        Element nodeElement = mock(Element.class);
        Evaluator evaluator1 = mock(Evaluator.class);
        Evaluator evaluator2 = mock(Evaluator.class);
        when(evaluator1.matches(rootElement, nodeElement)).thenReturn(false);
        when(evaluator2.matches(rootElement, nodeElement)).thenReturn(true);
        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or(evaluator1, evaluator2);

        // Act
        boolean result = orEvaluator.matches(rootElement, nodeElement);

        // Assert
        assertTrue(result);
    }

    @Test
    void testCombiningEvaluator_Or_Matches_ReturnsFalse() {
        // Arrange
        Element rootElement = mock(Element.class);
        Element nodeElement = mock(Element.class);
        Evaluator evaluator1 = mock(Evaluator.class);
        Evaluator evaluator2 = mock(Evaluator.class);
        when(evaluator1.matches(rootElement, nodeElement)).thenReturn(false);
        when(evaluator2.matches(rootElement, nodeElement)).thenReturn(false);
        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or(evaluator1, evaluator2);

        // Act
        boolean result = orEvaluator.matches(rootElement, nodeElement);

        // Assert
        assertFalse(result);
    }

    @Test
    void testCombiningEvaluator_Or_Add_Matches_ReturnsTrue() {
        // Arrange
        Element rootElement = mock(Element.class);
        Element nodeElement = mock(Element.class);
        Evaluator evaluator1 = mock(Evaluator.class);
        Evaluator evaluator2 = mock(Evaluator.class);
        Evaluator evaluator3 = mock(Evaluator.class);
        when(evaluator1.matches(rootElement, nodeElement)).thenReturn(false);
        when(evaluator2.matches(rootElement, nodeElement)).thenReturn(false);
        when(evaluator3.matches(rootElement, nodeElement)).thenReturn(true);
        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or(evaluator1, evaluator2);
        orEvaluator.add(evaluator3);

        // Act
        boolean result = orEvaluator.matches(rootElement, nodeElement);

        // Assert
        assertTrue(result);
    }

    @Test
    void testCombiningEvaluator_Or_TypicalUseCase() {
        // Arrange
        Element rootElement = mock(Element.class);
        Element nodeElement = mock(Element.class);
        Evaluator evaluator1 = mock(Evaluator.class);
        Evaluator evaluator2 = mock(Evaluator.class);
        Evaluator evaluator3 = mock(Evaluator.class);
        when(evaluator1.matches(rootElement, nodeElement)).thenReturn(false);
        when(evaluator2.matches(rootElement, nodeElement)).thenReturn(true);
        when(evaluator3.matches(rootElement, nodeElement)).thenReturn(false);
        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or(evaluator1, evaluator2);
        orEvaluator.add(evaluator3);

        // Act
        boolean result = orEvaluator.matches(rootElement, nodeElement);

        // Assert
        assertTrue(result);
    }

    @Test
    void testCombiningEvaluator_Or_EdgeCase_EmptyCollection() {
        // Arrange
        Element rootElement = mock(Element.class);
        Element nodeElement = mock(Element.class);
        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or(new ArrayList<>());

        // Act
        boolean result = orEvaluator.matches(rootElement, nodeElement);

        // Assert
        assertFalse(result);
    }

    @Test
    void testCombiningEvaluator_Or_ErrorScenario_NullEvaluators() {
        // Arrange
        Element rootElement = mock(Element.class);
        Element nodeElement = mock(Element.class);
        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or(null);

        // Act
        Exception exception = assertThrows(NullPointerException.class, () -> orEvaluator.matches(rootElement, nodeElement));

        // Assert
        assertEquals("evaluators cannot be null", exception.getMessage());
    }

    @Test
    void testCombiningEvaluator_And_ToString() {
        // Arrange
        Evaluator evaluator1 = Mockito.mock(Evaluator.class);
        Evaluator evaluator2 = Mockito.mock(Evaluator.class);
        when(evaluator1.toString()).thenReturn("evaluator1");
        when(evaluator2.toString()).thenReturn("evaluator2");
        CombiningEvaluator.And andEvaluator = new CombiningEvaluator.And(evaluator1, evaluator2);

        // Act
        String result = andEvaluator.toString();

        // Assert
        assertEquals("evaluator1 evaluator2", result);
    }

    @Test
    void testCombiningEvaluator_Or_ToString() {
        // Arrange
        Evaluator evaluator1 = Mockito.mock(Evaluator.class);
        when(evaluator1.toString()).thenReturn("evaluator1");
        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or(evaluator1);

        // Act
        String result = orEvaluator.toString();

        // Assert
        assertEquals(":or[evaluator1]", result);
    }
}