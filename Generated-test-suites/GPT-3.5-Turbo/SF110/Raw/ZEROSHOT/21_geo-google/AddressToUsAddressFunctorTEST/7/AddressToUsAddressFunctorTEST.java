import org.junit.*;
import static org.junit.Assert.*;

public class AddressToUsAddressFunctorTest {

    @Test
    public void testExecute() {
        // create test data
        GeoAddress addr = new GeoAddress();
        AddressDetails.Country country = new AddressDetails.Country();
        AddressDetails.Country.CountryNameCode countryNameCode = new AddressDetails.Country.CountryNameCode();
        countryNameCode.setContent("USA");
        country.getCountryNameCode().add(countryNameCode);
        AdministrativeArea adminArea = new AdministrativeArea();
        AdministrativeArea.AdministrativeAreaName adminAreaName = new AdministrativeArea.AdministrativeAreaName();
        adminAreaName.setContent("California");
        adminArea.getAdministrativeAreaName().add(adminAreaName);
        country.setAdministrativeArea(adminArea);
        addr.setAddressDetails(new AddressDetails());
        addr.getAddressDetails().setCountry(country);
        
        // execute the method under test
        AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();
        GeoUsAddress result = functor.execute(addr);
        
        // assert the result
        assertNotNull(result);
        assertEquals("USA", result.getCountry());
        assertEquals("California", result.getState());
    }

}