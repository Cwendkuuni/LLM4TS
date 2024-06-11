import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import geo.google.datamodel.*;
import geo.google.*;
import org.apache.commons.collections.*;
import oasis.names.tc.ciq.xsdschema.xal._2.*;
import java.util.*;

public class AddressToUsAddressFunctorTest {

    private AddressToUsAddressFunctor functor;
    private GeoAddress address;

    @Before
    public void setUp() {
        functor = AddressToUsAddressFunctor.getInstance();
        address = new GeoAddress();
    }

    @Test
    public void testExecute_ValidAddress() {
        // Typical use case - Valid address
        AddressDetails addressDetails = new AddressDetails();
        AddressDetails.Country country = new AddressDetails.Country();
        AddressDetails.Country.CountryNameCode countryCode = new AddressDetails.Country.CountryNameCode();
        countryCode.setContent("US");
        country.getCountryNameCode().add(countryCode);
        AdministrativeArea adminArea = new AdministrativeArea();
        AdministrativeArea.AdministrativeAreaName areaName = new AdministrativeArea.AdministrativeAreaName();
        areaName.setContent("California");
        adminArea.getAdministrativeAreaName().add(areaName);
        Locality locality = new Locality();
        Locality.LocalityName localityName = new Locality.LocalityName();
        localityName.setContent("San Francisco");
        locality.getLocalityName().add(localityName);
        PostalCode postalCode = new PostalCode();
        PostalCode.PostalCodeNumber postalCodeNumber = new PostalCode.PostalCodeNumber();
        postalCodeNumber.setContent("94107");
        postalCode.getPostalCodeNumber().add(postalCodeNumber);
        Thoroughfare thoroughfare = new Thoroughfare();
        Thoroughfare.ThoroughfareName thoroughfareName = new Thoroughfare.ThoroughfareName();
        thoroughfareName.setContent("Market St");
        thoroughfare.getThoroughfareName().add(thoroughfareName);
        locality.setThoroughfare(thoroughfare);
        adminArea.setLocality(locality);
        country.setAdministrativeArea(adminArea);
        addressDetails.setCountry(country);
        address.setAddressDetails(addressDetails);

        GeoUsAddress result = functor.execute(address);

        assertEquals("US", result.getCountry()); // Valid country code
        assertEquals("California", result.getState()); // Valid state
        assertEquals("San Francisco", result.getCity()); // Valid city
        assertEquals("94107", result.getPostalCode()); // Valid postal code
        assertEquals("Market St", result.getAddressLine1()); // Valid address line 1
        assertNull(result.getAddressLine2()); // Address line 2 is null
    }

    @Test
    public void testExecute_EmptyAddress() {
        // Error scenario - Empty address
        GeoUsAddress result = functor.execute(address);

        assertNull(result.getCountry()); // Country is null
        assertNull(result.getState()); // State is null
        assertNull(result.getCity()); // City is null
        assertNull(result.getPostalCode()); // Postal code is null
        assertNull(result.getAddressLine1()); // Address line 1 is null
        assertNull(result.getAddressLine2()); // Address line 2 is null
    }

    @Test
    public void testExecute_MissingCountryCode() {
        // Error scenario - Missing country code
        AddressDetails addressDetails = new AddressDetails();
        AddressDetails.Country country = new AddressDetails.Country();
        addressDetails.setCountry(country);
        address.setAddressDetails(addressDetails);

        GeoUsAddress result = functor.execute(address);

        assertNull(result.getCountry()); // Country code is missing
        assertNull(result.getState()); // State is null
        assertNull(result.getCity()); // City is null
        assertNull(result.getPostalCode()); // Postal code is null
        assertNull(result.getAddressLine1()); // Address line 1 is null
        assertNull(result.getAddressLine2()); // Address line 2 is null
    }

    @Test
    public void testExecute_MissingLocality() {
        // Error scenario - Missing locality
        AddressDetails addressDetails = new AddressDetails();
        AddressDetails.Country country = new AddressDetails.Country();
        AddressDetails.Country.CountryNameCode countryCode = new AddressDetails.Country.CountryNameCode();
        countryCode.setContent("US");
        country.getCountryNameCode().add(countryCode);
        AdministrativeArea adminArea = new AdministrativeArea();
        AdministrativeArea.AdministrativeAreaName areaName = new AdministrativeArea.AdministrativeAreaName();
        areaName.setContent("California");
        adminArea.getAdministrativeAreaName().add(areaName);
        country.setAdministrativeArea(adminArea);
        addressDetails.setCountry(country);
        address.setAddressDetails(addressDetails);

        GeoUsAddress result = functor.execute(address);

        assertEquals("US", result.getCountry()); // Valid country code
        assertEquals("California", result.getState()); // Valid state
        assertNull(result.getCity()); // City is null
        assertNull(result.getPostalCode()); // Postal code is null
        assertNull(result.getAddressLine1()); // Address line 1 is null
        assertNull(result.getAddressLine2()); // Address line 2 is null
    }
}