package geo.google.mapping;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import geo.google.datamodel.AddressDetails;
import geo.google.datamodel.AdministrativeArea;
import geo.google.datamodel.GeoAddress;
import geo.google.datamodel.GeoUsAddress;
import geo.google.datamodel.Locality;
import geo.google.datamodel.PostalCode;
import geo.google.datamodel.ThoroughfareNameType;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Before;
import org.junit.Test;

public class AddressToUsAddressFunctorTest {

    private AddressToUsAddressFunctor addressToUsAddressFunctor;
    private GeoAddress geoAddress;

    @Before
    public void setup() {
        addressToUsAddressFunctor = AddressToUsAddressFunctor.getInstance();
        geoAddress = mock(GeoAddress.class);
    }

    @Test
    public void testExecute() {
        GeoUsAddress expected = new GeoUsAddress();
        expected.setCountry("Some Country");
        expected.setState("Some State");
        expected.setCounty("Some County");
        expected.setCity("Some City");
        expected.setPostalCode("12345");
        expected.setAddressLine1("Some Address 1");
        expected.setAddressLine2("Some Address 2");
        expected.setCoordinate(1, 1);
        expected.setAccuracy(0.1);

        AddressDetails.Country country = mock(AddressDetails.Country.class);
        when(geoAddress.getAddressDetails().getCountry()).thenReturn(country);

        AddressDetails.Country.CountryNameCode countryNameCode = mock(AddressDetails.Country.CountryNameCode.class);
        when(country.getCountryNameCode()).thenReturn(
            Collections.singletonList(countryNameCode));
        when(countryNameCode.getContent()).thenReturn("Some Country");

        AdministrativeArea adminArea = mock(AdministrativeArea.class);
        when(country.getAdministrativeArea()).thenReturn(adminArea);

        AdministrativeArea.AdministrativeAreaName adminAreaName = mock(AdministrativeArea.AdministrativeAreaName.class);
        when(adminArea.getAdministrativeAreaName()).thenReturn(
            Collections.singletonList(adminAreaName));
        when(adminAreaName.getContent()).thenReturn("Some State");

        AdministrativeArea.SubAdministrativeArea subAdminArea = mock(AdministrativeArea.SubAdministrativeArea.class);
        when(adminArea.getSubAdministrativeArea()).thenReturn(subAdminArea);

        AdministrativeArea.SubAdministrativeArea.SubAdministrativeAreaName subAdminAreaName = mock(AdministrativeArea.SubAdministrativeArea.SubAdministrativeAreaName.class);
        when(subAdminArea.getSubAdministrativeAreaName()).thenReturn(
            Collections.singletonList(subAdminAreaName));
        when(subAdminAreaName.getContent()).thenReturn("Some County");

        Locality locality = mock(Locality.class);
        when(subAdminArea.getLocality()).thenReturn(locality);

        Locality.LocalityName localityName = mock(Locality.LocalityName.class);
        when(locality.getLocalityName()).thenReturn(
            Collections.singletonList(localityName));
        when(localityName.getContent()).thenReturn("Some City");

        PostalCode postalCode = mock(PostalCode.class);
        when(locality.getPostalCode()).thenReturn(postalCode);

        PostalCode.PostalCodeNumber postalCodeNumber = mock(PostalCode.PostalCodeNumber.class);
        when(postalCode.getPostalCodeNumber()).thenReturn(
            Collections.singletonList(postalCodeNumber));
        when(postalCodeNumber.getContent()).thenReturn("12345");

        ThoroughfareNameType thoroughfare = mock(ThoroughfareNameType.class);
        when(locality.getThoroughfare().getThoroughfareName()).thenReturn(
            Collections.singletonList(thoroughfare));
        when(thoroughfare.getContent()).thenReturn("Some Address 1");

        ThoroughfareNameType thoroughfare2 = mock(ThoroughfareNameType.class);
        when(locality.getThoroughfare().getThoroughfareName().size()).thenReturn(2);
        when(locality.getThoroughfare().getThoroughfareName().get(1)).thenReturn(thoroughfare2);
        when(thoroughfare2.getContent()).thenReturn("Some Address 2");

        when(geoAddress.getCoordinate()).thenReturn(mock(GeoAddress.Coordinate.class));
        when(geoAddress.getCoordinate().clone()).thenReturn(mock(GeoAddress.Coordinate.class));
        when(geoAddress.getAccuracy()).thenReturn(0.1);

        GeoUsAddress result = addressToUsAddressFunctor.execute(geoAddress);

        assertEquals(expected, result);
    }
}