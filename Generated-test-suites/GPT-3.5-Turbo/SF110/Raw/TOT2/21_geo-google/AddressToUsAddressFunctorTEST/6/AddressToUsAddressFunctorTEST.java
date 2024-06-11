import static org.junit.Assert.*;
import org.junit.Test;

public class AddressToUsAddressFunctorTest {

    @Test
    public void testExecute_WithNullAddress_ReturnsEmptyUsAddress() {
        AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();
        GeoUsAddress result = functor.execute(null);
        assertNull(result.getCountry());
        assertNull(result.getState());
        assertNull(result.getCounty());
        assertNull(result.getCity());
        assertNull(result.getPostalCode());
        assertNull(result.getAddressLine1());
        assertNull(result.getAddressLine2());
    }

    @Test
    public void testExecute_WithCompleteAddress_CreatesUsAddressSuccessfully() {
        AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();
        GeoAddress address = new GeoAddress();
        AddressDetails addressDetails = new AddressDetails();
        AddressDetails.Country country = new AddressDetails.Country();
        country.addCountryNameCode(new AddressDetails.Country.CountryNameCode("US"));
        addressDetails.setCountry(country);
        address.setAddressDetails(addressDetails);

        GeoUsAddress result = functor.execute(address);

        assertEquals("US", result.getCountry());
    }

    @Test
    public void testExecute_WithNullCoordinate_SetsAccuracyAndReturnsUsAddress() {
        AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();
        GeoAddress address = new GeoAddress();
        address.setAccuracy(0.85);

        GeoUsAddress result = functor.execute(address);

        assertEquals(0.85, result.getAccuracy(), 0.001);
        assertNull(result.getCoordinate());
    }

    @Test(expected = NullPointerException.class)
    public void testExecute_WithNullCoordinate_ThrowsNullPointerException() {
        AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();
        GeoAddress address = new GeoAddress();
        address.setCoordinate(null);

        functor.execute(address);
    }
}