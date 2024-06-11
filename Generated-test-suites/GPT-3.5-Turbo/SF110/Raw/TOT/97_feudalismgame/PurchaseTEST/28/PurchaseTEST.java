import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class PurchaseTest {

    private Purchase purchase;
    private List<String> output;

    @Before
    public void setup() {
        purchase = new Purchase();
        output = new ArrayList<>();
    }

    @Test
    public void testPerform_withValidArgs_shouldInvokeMethods() {
        // Arrange
        Iterator<String> args = Arrays.asList("vassal", "10", "buyer", "fiefdom").iterator();

        // Act
        purchase.perform(args);

        // Assert
        // Verify that the vassal method is invoked and the output is correct
        //...

        // Verify other methods are not invoked
        //...
    }

    @Test
    public void testVassal_withValidArgsAndKing_shouldBuyVassals() {
        // Arrange
        Iterator<String> arg = Arrays.asList("10", "buyer", "fiefdom").iterator();

        // Act
        purchase.vassal(arg);

        // Assert
        // Verify that the vassals are bought correctly
        //...
    }

    @Test
    public void testVassal_withInvalidArgsAndKing_shouldPrintErrorMessage() {
        // Arrange
        Iterator<String> arg = Arrays.asList("invalid", "buyer", "fiefdom").iterator();

        // Act
        purchase.vassal(arg);

        // Assert
        // Verify that the error message is printed correctly
        //...
    }

    // Repeat the test method for other methods (e.g., castleFiefdom, castlePersonal, mercenary)

    @Test
    public void testMercenary_withValidArgsAndKing_shouldBuyMercenaries() {
        // Arrange
        Iterator<String> arg = Arrays.asList("10", "buyer", "knight").iterator();

        // Act
        purchase.mercenary(arg);

        // Assert
        // Verify that the mercenaries are bought correctly
        //...
    }

    @Test
    public void testMercenary_withInvalidArgs_shouldPrintErrorMessage() {
        // Arrange
        Iterator<String> arg = Arrays.asList("invalid", "buyer", "knight").iterator();

        // Act
        purchase.mercenary(arg);

        // Assert
        // Verify that the error message is printed correctly
        //...
    }

    // Add more test cases for all methods
    //...

    @Test
    public void testPerform_withInvalidMethod_shouldPrintErrorMessage() {
        // Arrange
        Iterator<String> args = Arrays.asList("invalidMethod").iterator();

        // Act
        purchase.perform(args);

        // Assert
        // Verify that the error message is printed correctly
        //...
    }

    // Add more negative test cases for the perform method
    //...

}