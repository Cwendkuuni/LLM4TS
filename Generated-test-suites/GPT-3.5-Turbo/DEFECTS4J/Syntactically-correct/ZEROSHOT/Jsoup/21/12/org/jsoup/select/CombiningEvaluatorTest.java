
package org.jsoup.select;

import org.jsoup.nodes.Element;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.*;

public class CombiningEvaluatorTest {

    @Test
    public void testAndMatches() {
        // Arrange
        Element root = new Element("");
        Element node = new Element("");
        CombiningEvaluator.And evaluator = new CombiningEvaluator.And(new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return true;
            }
        });

        // Act
        boolean result = evaluator.matches(root, node);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testAndNotMatches() {
        // Arrange
        Element root = new Element("");
        Element node = new Element("");
        CombiningEvaluator.And evaluator = new CombiningEvaluator.And(new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return false;
            }
        });

        // Act
        boolean result = evaluator.matches(root, node);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testAndToString() {
        // Arrange
        CombiningEvaluator.And evaluator = new CombiningEvaluator.And(new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return true;
            }
        });

        // Act
        String result = evaluator.toString();

        // Assert
        assertEquals(" ", result);
    }

    @Test
    public void testOrMatches() {
        // Arrange
        Element root = new Element("");
        Element node = new Element("");
        CombiningEvaluator.Or evaluator = new CombiningEvaluator.Or(new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return true;
            }
        });

        // Act
        boolean result = evaluator.matches(root, node);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testOrNotMatches() {
        // Arrange
        Element root = new Element("");
        Element node = new Element("");
        CombiningEvaluator.Or evaluator = new CombiningEvaluator.Or(new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return false;
            }
        });

        // Act
        boolean result = evaluator.matches(root, node);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testOrAdd() {
        // Arrange
        Element root = new Element("");
        Element node = new Element("");
        CombiningEvaluator.Or evaluator = new CombiningEvaluator.Or(new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return true;
            }
        });

        // Act
        evaluator.add(new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return true;
            }
        });

        boolean result = evaluator.matches(root, node);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testOrToString() {
        // Arrange
        CombiningEvaluator.Or evaluator = new CombiningEvaluator.Or(new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return true;
            }
        });

        // Act
        String result = evaluator.toString();

        // Assert
        assertEquals(":or[]", result);
    }
}
