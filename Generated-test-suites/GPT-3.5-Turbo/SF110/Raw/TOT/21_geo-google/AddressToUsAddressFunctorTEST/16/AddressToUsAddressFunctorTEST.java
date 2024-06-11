import static org.junit.Assert.*;
import org.junit.Test;
import geo.google.datamodel.*;
import geo.google.*;
import oasis.names.tc.ciq.xsdschema.xal._2.*;
import java.util.*;

public class AddressToUsAddressFunctorTest {

    @Test
    public void testExecute_withValidAddress_returnsGeoUsAddress() {
        // Arrange
        AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();
        GeoAddress address = new GeoAddress();

        AddressDetails addressDetails = new AddressDetails();
        address.setAddressDetails(addressDetails);

        // Set up country
        AddressDetails.Country country = new AddressDetails.Country();
        addressDetails.setCountry(country);

        AddressDetails.Country.CountryNameCode countryNameCode = new AddressDetails.Country.CountryNameCode();
        countryNameCode.setContent("US");
        country.getCountryNameCode().add(countryNameCode);

        // Set up administrative area
        AdministrativeArea adminArea = new AdministrativeArea();
        country.setAdministrativeArea(adminArea);

        AdministrativeArea.AdministrativeAreaName adminAreaName = new AdministrativeArea.AdministrativeAreaName();
        adminAreaName.setContent("California");
        adminArea.getAdministrativeAreaName().add(adminAreaName);

        // Set up locality
        Locality locality = new Locality();
        adminArea.setLocality(locality);

        Locality.LocalityName localityName = new Locality.LocalityName();
        localityName.setContent("San Francisco");
        locality.getLocalityName().add(localityName);

        // Set up postal code
        PostalCode postalCode = new PostalCode();
        locality.setPostalCode(postalCode);

        PostalCode.PostalCodeNumber postalCodeNumber = new PostalCode.PostalCodeNumber();
        postalCodeNumber.setContent("94107");
        postalCode.getPostalCodeNumber().add(postalCodeNumber);

        // Set up thoroughfare
        Thoroughfare thoroughfare = new Thoroughfare();
        locality.setThoroughfare(thoroughfare);

        ThoroughfareNameType thoroughfareName = new ThoroughfareNameType();
        thoroughfareName.setContent("Mission St");
        thoroughfare.getThoroughfareName().add(thoroughfareName);

        // Act
        GeoUsAddress result = functor.execute(address);

        // Assert
        assertNotNull(result);
        assertEquals("US", result.getCountry());
        assertEquals("California", result.getState());
        assertEquals("San Francisco", result.getCity());
        assertEquals("94107", result.getPostalCode());
        assertEquals("Mission St", result.getAddressLine1());
    }

    @Test
    public void testExecute_withNoAddressDetails_returnsEmptyGeoUsAddress() {
        // Arrange
        AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();
        GeoAddress address = new GeoAddress();

        // Act
        GeoUsAddress result = functor.execute(address);

        // Assert
        assertNotNull(result);
        assertNull(result.getCountry());
        assertNull(result.getState());
        assertNull(result.getCity());
        assertNull(result.getPostalCode());
        assertNull(result.getAddressLine1());
    }

    @Test
    public void testExecute_withInvalidAddress_returnsEmptyGeoUsAddress() {
        // Arrange
        AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();
        GeoAddress address = null;

        // Act
        GeoUsAddress result = functor.execute(address);

        // Assert
        assertNotNull(result);
        assertNull(result.getCountry());
        assertNull(result.getState());
        assertNull(result.getCity());
        assertNull(result.getPostalCode());
        assertNull(result.getAddressLine1());
    }

    @Test
    public void testExecute_withEmptyCountry_returnsEmptyGeoUsAddress() {
        // Arrange
        AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();
        GeoAddress address = new GeoAddress();

        AddressDetails addressDetails = new AddressDetails();
        address.setAddressDetails(addressDetails);

        // Set up country
        AddressDetails.Country country = new AddressDetails.Country();
        addressDetails.setCountry(country);

        // No country name code

        // Act
        GeoUsAddress result = functor.execute(address);

        // Assert
        assertNotNull(result);
        assertNull(result.getCountry());
        assertNull(result.getState());
        assertNull(result.getCity());
        assertNull(result.getPostalCode());
        assertNull(result.getAddressLine1());
    }

    @Test
    public void testExecute_withEmptyAdminArea_returnsGeoUsAddressWithStateNull() {
        // Arrange
        AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();
        GeoAddress address = new GeoAddress();

        AddressDetails addressDetails = new AddressDetails();
        address.setAddressDetails(addressDetails);

        // Set up country
        AddressDetails.Country country = new AddressDetails.Country();
        addressDetails.setCountry(country);

        AddressDetails.Country.CountryNameCode countryNameCode = new AddressDetails.Country.CountryNameCode();
        countryNameCode.setContent("US");
        country.getCountryNameCode().add(countryNameCode);

        // No administrative area

        // Act
        GeoUsAddress result = functor.execute(address);

        // Assert
        assertNotNull(result);
        assertEquals("US", result.getCountry());
        assertNull(result.getState());
        assertNull(result.getCity());
        assertNull(result.getPostalCode());
        assertNull(result.getAddressLine1());
    }
}