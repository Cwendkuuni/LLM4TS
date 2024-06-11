import org.junit.*;

public class PurchaseTest {

    @Test
    public void testVassal() {
        // Test case 1
        // Input: [5, "buyer1", "fiefdom1"]
        // Expected output: no output
        Purchase purchase = new Purchase();
        Iterator arg = Arrays.asList(5, "buyer1", "fiefdom1").iterator();
        purchase.vassal(arg);

        // Test case 2
        // Input: [10, "buyer2", "fiefdom2"]
        // Expected output: "You cannot buy more vassals, you have insufficient funds"
        Purchase purchase = new Purchase();
        Iterator arg = Arrays.asList(10, "buyer2", "fiefdom2").iterator();
        purchase.vassal(arg);
    }

    @Test
    public void testCastleFiefdom() {
        // Test case 1
        // Input: [3, "buyer1", "fiefdom1"]
        // Expected output: no output
        Purchase purchase = new Purchase();
        Iterator arg = Arrays.asList(3, "buyer1", "fiefdom1").iterator();
        purchase.castleFiefdom(arg);

        // Test case 2
        // Input: [8, "buyer2", "fiefdom2"]
        // Expected output: "You cannot buy more Fiefdom castles, you have insufficient funds"
        Purchase purchase = new Purchase();
        Iterator arg = Arrays.asList(8, "buyer2", "fiefdom2").iterator();
        purchase.castleFiefdom(arg);
    }

    @Test
    public void testCastlePersonal() {
        // Test case 1
        // Input: [2, "buyer1", "fiefdom1"]
        // Expected output: no output
        Purchase purchase = new Purchase();
        Iterator arg = Arrays.asList(2, "buyer1", "fiefdom1").iterator();
        purchase.castlePersonal(arg);

        // Test case 2
        // Input: [6, "buyer2", "fiefdom2"]
        // Expected output: "You cannot buy more Personal castle, you have insufficient funds"
        Purchase purchase = new Purchase();
        Iterator arg = Arrays.asList(6, "buyer2", "fiefdom2").iterator();
        purchase.castlePersonal(arg);
    }

    @Test
    public void testMercenary() {
        // Test case 1
        // Input: [4, "buyer1", "knight1"]
        // Expected output: no output
        Purchase purchase = new Purchase();
        Iterator arg = Arrays.asList(4, "buyer1", "knight1").iterator();
        purchase.mercenary(arg);

        // Test case 2
        // Input: [9, "buyer2", "knight2"]
        // Expected output: no output
        Purchase purchase = new Purchase();
        Iterator arg = Arrays.asList(9, "buyer2", "knight2").iterator();
        purchase.mercenary(arg);

        // Test case 3
        // Input: [7, "buyer3", "knight3"]
        // Expected output: "Inexistent Knight"
        Purchase purchase = new Purchase();
        Iterator arg = Arrays.asList(7, "buyer3", "knight3").iterator();
        purchase.mercenary(arg);
    }
}