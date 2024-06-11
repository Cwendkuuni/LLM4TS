import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

public class IndexedStringTest {

    @Test
    public void count_WhenCalledWithValidInputs_ReturnsCorrectCount() {
        // Arrange
        IndexedString indexedString = new IndexedString("Hello World");
        
        // Act
        int count = indexedString.count('l');
        
        // Assert
        assertEquals(3, count);
    }
    
    @Test
    public void count_WhenCalledWithInvalidInput_ReturnsZero() {
        // Arrange
        IndexedString indexedString = new IndexedString("Hello World");
        
        // Act
        int count = indexedString.count('z');
        
        // Assert
        assertEquals(0, count);
    }
    
    @Test
    public void indexOf_WhenCalledWithValidTarget_ReturnsCorrectIndex() {
        // Arrange
        IndexedString indexedString = new IndexedString("Hello World");
        char[] target = {'l','o',' ','W','o','r','l','d'};
        
        // Act
        int index = indexedString.indexOf(target);
        
        // Assert
        assertEquals(2, index);
    }
    
    @Test
    public void indexOf_WhenCalledWithInvalidTarget_ReturnsNegativeIndex() {
        // Arrange
        IndexedString indexedString = new IndexedString("Hello World");
        char[] target = {'z','x','y'};
        
        // Act
        int index = indexedString.indexOf(target);
        
        // Assert
        assertEquals(-1, index);
    }
    
    @Test
    public void indexOf_WhenCalledWithPartialTarget_ReturnsNegativeIndex() {
        // Arrange
        IndexedString indexedString = new IndexedString("Hello World");
        char[] target = {'l','o','w'};
        
        // Act
        int index = indexedString.indexOf(target);
        
        // Assert
        assertEquals(-1, index);
    }
    
    @Test
    public void indexOf_WhenCalledWithEmptyTarget_ReturnsNegativeIndex() {
        // Arrange
        IndexedString indexedString = new IndexedString("Hello World");
        char[] target = {};
        
        // Act
        int index = indexedString.indexOf(target);
        
        // Assert
        assertEquals(-1, index);
    }
    
    @Test
    public void indexOf_WhenCalledWithLongTarget_ReturnsNegativeIndex() {
        // Arrange
        IndexedString indexedString = new IndexedString("Hello World");
        char[] target = {'l','o',' ','W','o','r','l','d',' ','W','o','r','l','d'};
        
        // Act
        int index = indexedString.indexOf(target);
        
        // Assert
        assertEquals(-1, index);
    }
    
    @Test
    public void indexOf_WhenCalledWithTargetLargerThanString_ReturnsNegativeIndex() {
        // Arrange
        IndexedString indexedString = new IndexedString("Hello World");
        char[] target = {'H','e','l','l','o',' ','W','o','r','l','d',' ','W','o','r','l','d'};
        
        // Act
        int index = indexedString.indexOf(target);
        
        // Assert
        assertEquals(-1, index);
    }
    
    @Test
    public void indexOf_WhenCalledWithSpecialCharacters_ReturnsCorrectIndex() {
        // Arrange
        IndexedString indexedString = new IndexedString("Hello World!@#$%^&*()");
        char[] target = {'!','@','#','$','%','^','&','*','(',')'};
        
        // Act
        int index = indexedString.indexOf(target);
        
        // Assert
        assertEquals(12, index);
    }
    
    @Test
    public void indexOf_WhenCalledWithEmptyString_ReturnsNegativeIndex() {
        // Arrange
        IndexedString indexedString = new IndexedString("");
        char[] target = {'H','e','l','l','o'};
        
        // Act
        int index = indexedString.indexOf(target);
        
        // Assert
        assertEquals(-1, index);
    }
    
}