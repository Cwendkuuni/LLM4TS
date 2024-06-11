
package geo.google.mapping;

import geo.google.datamodel.*;
import geo.google.*;
import org.apache.commons.collections.*;
import oasis.names.tc.ciq.xsdschema.xal._2.*;
import java.util.*;

import org.junit.*;
import static org.junit.Assert.*;

public class AddressToUsAddressFunctorTest {

    private AddressToUsAddressFunctor addressToUsAddressFunctor;

    @Before
    public void setUp() {
        addressToUsAddressFunctor = AddressToUsAddressFunctor.getInstance();
    }

    @Test
    public void testExecute_WithValidGeoAddress_ShouldReturnValidGeoUsAddress() {
        // Arrange
        GeoAddress geoAddress = new GeoAddress();
        AddressDetails addressDetails = new AddressDetails();
        geoAddress.setAddressDetails(addressDetails);

        AddressDetails.Country country = new AddressDetails.Country();
        addressDetails.setCountry(country);

        AddressDetails.Country.CountryNameCode countryNameCode = new AddressDetails.Country.CountryNameCode();
        countryNameCode.setContent("US");
        country.getCountryNameCode().add(countryNameCode);

        AdministrativeArea adminArea = new AdministrativeArea();
        country.setAdministrativeArea(adminArea);

        AdministrativeArea.AdministrativeAreaName adminAreaName = new AdministrativeArea.AdministrativeAreaName();
        adminAreaName.setContent("California");
        adminArea.getAdministrativeAreaName().add(adminAreaName);

        Locality locality = new Locality();
        adminArea.setLocality(locality);

        Locality.LocalityName localityName = new Locality.LocalityName();
        localityName.setContent("Los Angeles");
        locality.getLocalityName().add(localityName);

        PostalCode postalCode = new PostalCode();
        locality.setPostalCode(postalCode);

        PostalCode.PostalCodeNumber postalCodeNumber = new PostalCode.PostalCodeNumber();
        postalCodeNumber.setContent("90001");
        postalCode.getPostalCodeNumber().add(postalCodeNumber);

        Thoroughfare thoroughfare = new Thoroughfare();
        locality.setThoroughfare(thoroughfare);

        ThoroughfareNameType thoroughfareNameType1 = new ThoroughfareNameType();
        thoroughfareNameType1.setContent("Main St");
        thoroughfare.getThoroughfareName().add(thoroughfareNameType1);

        ThoroughfareNameType thoroughfareNameType2 = new ThoroughfareNameType();
        thoroughfareNameType2.setContent("Apt 20");
        thoroughfare.getThoroughfareName().add(thoroughfareNameType2);

        // Act
        GeoUsAddress geoUsAddress = addressToUsAddressFunctor.execute(geoAddress);

        // Assert
        assertNotNull(geoUsAddress);
        assertEquals("US", geoUsAddress.getCountry());
        assertEquals("California", geoUsAddress.getState());
        assertEquals("Los Angeles", geoUsAddress.getCity());
        assertEquals("90001", geoUsAddress.getPostalCode());
        assertEquals("Main St", geoUsAddress.getAddressLine1());
        assertEquals("Apt 20", geoUsAddress.getAddressLine2());
    }

    @Test
    public void testExecute_WithGeoAddressWithoutCountry_ShouldReturnEmptyGeoUsAddress() {
        // Arrange
        GeoAddress geoAddress = new GeoAddress();
        AddressDetails addressDetails = new AddressDetails();
        geoAddress.setAddressDetails(addressDetails);

        // Act
        GeoUsAddress geoUsAddress = addressToUsAddressFunctor.execute(geoAddress);

        // Assert
        assertNotNull(geoUsAddress);
        assertNull(geoUsAddress.getCountry());
        assertNull(geoUsAddress.getState());
        assertNull(geoUsAddress.getCity());
        assertNull(geoUsAddress.getPostalCode());
        assertNull(geoUsAddress.getAddressLine1());
        assertNull(geoUsAddress.getAddressLine2());
    }
    
    @Test
    public void testExecute_WithGeoAddressWithoutState_ShouldReturnPartialGeoUsAddress() {
        // Arrange
        GeoAddress geoAddress = new GeoAddress();
        AddressDetails addressDetails = new AddressDetails();
        geoAddress.setAddressDetails(addressDetails);

        AddressDetails.Country country = new AddressDetails.Country();
        addressDetails.setCountry(country);

        AddressDetails.Country.CountryNameCode countryNameCode = new AddressDetails.Country.CountryNameCode();
        countryNameCode.setContent("US");
        country.getCountryNameCode().add(countryNameCode);

        // Act
        GeoUsAddress geoUsAddress = addressToUsAddressFunctor.execute(geoAddress);

        // Assert
        assertNotNull(geoUsAddress);
        assertEquals("US", geoUsAddress.getCountry());
        assertNull(geoUsAddress.getState());
        assertNull(geoUsAddress.getCity());
        assertNull(geoUsAddress.getPostalCode());
        assertNull(geoUsAddress.getAddressLine1());
        assertNull(geoUsAddress.getAddressLine2());
    }

    @Test
    public void testExecute_WithGeoAddressWithoutLocality_ShouldReturnPartialGeoUsAddress() {
        // Arrange
        GeoAddress geoAddress = new GeoAddress();
        AddressDetails addressDetails = new AddressDetails();
        geoAddress.setAddressDetails(addressDetails);

        AddressDetails.Country country = new AddressDetails.Country();
        addressDetails.setCountry(country);

        AddressDetails.Country.CountryNameCode countryNameCode = new AddressDetails.Country.CountryNameCode();
        countryNameCode.setContent("US");
        country.getCountryNameCode().add(countryNameCode);

        AdministrativeArea adminArea = new AdministrativeArea();
        country.setAdministrativeArea(adminArea);

        AdministrativeArea.AdministrativeAreaName adminAreaName = new AdministrativeArea.AdministrativeAreaName();
        adminAreaName.setContent("California");
        adminArea.getAdministrativeAreaName().add(adminAreaName);

        // Act
        GeoUsAddress geoUsAddress = addressToUsAddressFunctor.execute(geoAddress);

        // Assert
        assertNotNull(geoUsAddress);
        assertEquals("US", geoUsAddress.getCountry());
        assertEquals("California", geoUsAddress.getState());
        assertNull(geoUsAddress.getCity());
        assertNull(geoUsAddress.getPostalCode());
        assertNull(geoUsAddress.getAddressLine1());
        assertNull(geoUsAddress.getAddressLine2());
    }

    @Test
    public void testExecute_WithGeoAddressWithoutPostalCode_ShouldReturnPartialGeoUsAddress() {
        // Arrange
        GeoAddress geoAddress = new GeoAddress();
        AddressDetails addressDetails = new AddressDetails();
        geoAddress.setAddressDetails(addressDetails);

        AddressDetails.Country country = new AddressDetails.Country();
        addressDetails.setCountry(country);

        AddressDetails.Country.CountryNameCode countryNameCode = new AddressDetails.Country.CountryNameCode();
        countryNameCode.setContent("US");
        country.getCountryNameCode().add(countryNameCode);

        AdministrativeArea adminArea = new AdministrativeArea();
        country.setAdministrativeArea(adminArea);

        AdministrativeArea.AdministrativeAreaName adminAreaName = new AdministrativeArea.AdministrativeAreaName();
        adminAreaName.setContent("California");
        adminArea.getAdministrativeAreaName().add(adminAreaName);

        Locality locality = new Locality();
        adminArea.setLocality(locality);

        Locality.LocalityName localityName = new Locality.LocalityName();
        localityName.setContent("Los Angeles");
        locality.getLocalityName().add(localityName);

        // Act
        GeoUsAddress geoUsAddress = addressToUsAddressFunctor.execute(geoAddress);

        // Assert
        assertNotNull(geoUsAddress);
        assertEquals("US", geoUsAddress.getCountry());
        assertEquals("California", geoUsAddress.getState());
        assertEquals("Los Angeles", geoUsAddress.getCity());
        assertNull(geoUsAddress.getPostalCode());
        assertNull(geoUsAddress.getAddressLine1());
        assertNull(geoUsAddress.getAddressLine2());
    }

    @Test
    public void testExecute_WithGeoAddressWithoutThoroughfare_ShouldReturnPartialGeoUsAddress() {
        // Arrange
        GeoAddress geoAddress = new GeoAddress();
        AddressDetails addressDetails = new AddressDetails();
        geoAddress.setAddressDetails(addressDetails);

        AddressDetails.Country country = new AddressDetails.Country();
        addressDetails.setCountry(country);

        AddressDetails.Country.CountryNameCode countryNameCode = new AddressDetails.Country.CountryNameCode();
        countryNameCode.setContent("US");
        country.getCountryNameCode().add(countryNameCode);

        AdministrativeArea adminArea = new AdministrativeArea();
        country.setAdministrativeArea(adminArea);

        AdministrativeArea.AdministrativeAreaName adminAreaName = new AdministrativeArea.AdministrativeAreaName();
        adminAreaName.setContent("California");
        adminArea.getAdministrativeAreaName().add(adminAreaName);

        Locality locality = new Locality();
        adminArea.setLocality(locality);

        Locality.LocalityName localityName = new Locality.LocalityName();
        localityName.setContent("Los Angeles");
        locality.getLocalityName().add(localityName);

        PostalCode postalCode = new PostalCode();
        locality.setPostalCode(postalCode);

        PostalCode.PostalCodeNumber postalCodeNumber = new PostalCode.PostalCodeNumber();
        postalCodeNumber.setContent("90001");
        postalCode.getPostalCodeNumber().add(postalCodeNumber);

        // Act
        GeoUsAddress geoUsAddress = addressToUsAddressFunctor.execute(geoAddress);

        // Assert
        assertNotNull(geoUsAddress);
        assertEquals("US", geoUsAddress.getCountry());
        assertEquals("California", geoUsAddress.getState());
        assertEquals("Los Angeles", geoUsAddress.getCity());
        assertEquals("90001", geoUsAddress.getPostalCode());
        assertNull(geoUsAddress.getAddressLine1());
        assertNull(geoUsAddress.getAddressLine2());
    }

    @Test
    public void testExecute_WithNullAddress_ShouldReturnEmptyGeoUsAddress() {
        // Arrange
        GeoAddress geoAddress = null;

        // Act
        GeoUsAddress geoUsAddress = addressToUsAddressFunctor.execute(geoAddress);

        // Assert
        assertNotNull(geoUsAddress);
        assertNull(geoUsAddress.getCountry());
        assertNull(geoUsAddress.getState());
        assertNull(geoUsAddress.getCity());
        assertNull(geoUsAddress.getPostalCode());
        assertNull(geoUsAddress.getAddressLine1());
        assertNull(geoUsAddress.getAddressLine2());
    }
}

